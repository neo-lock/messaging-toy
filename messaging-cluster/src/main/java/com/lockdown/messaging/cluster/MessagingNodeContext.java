package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.command.SyncCommand;
import com.lockdown.messaging.cluster.event.LocalServerEventListener;
import com.lockdown.messaging.cluster.node.*;
import com.lockdown.messaging.cluster.utils.GlobalTimer;
import com.lockdown.messaging.cluster.utils.IPUtils;
import io.netty.channel.EventLoopGroup;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


public class MessagingNodeContext implements LocalServerEventListener {


    private final ServerDestination localDestination;
    private final MessagingProperties properties;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ExecutorService segmentGroup;
    private GlobalTimer globalTimer;
    private LocalNode localNode;
    private LocalClientRemoteNodeMonitor nodeMonitor;
    private GlobalCommandRouter commandRouter;
    private LocalServer localServer;
    private SyncCommandMonitor commandMonitor;


    public MessagingNodeContext(MessagingProperties properties) {
        this.properties = properties;
        this.localDestination = new ServerDestination(IPUtils.getLocalIP(), properties.getNodePort());
    }

    public void shutdownExecutor() {
        if (Objects.nonNull(bossGroup) && !bossGroup.isShutdown()) {
            bossGroup.shutdownGracefully();
        }
        if (Objects.nonNull(workerGroup) && !workerGroup.isShutdown()) {
            workerGroup.shutdownGracefully();
        }
        if (Objects.nonNull(segmentGroup) && !segmentGroup.isShutdown()) {
            segmentGroup.shutdown();
        }
    }



    public ServerDestination getLocalDestination() {
        return localDestination;
    }

    public GlobalCommandRouter getCommandRouter() {
        return commandRouter;
    }

    public MessagingProperties getProperties() {
        return properties;
    }


    public EventLoopGroup getBossGroup() {
        return bossGroup;
    }

    public MessagingNodeContext setBossGroup(EventLoopGroup bossGroup) {
        this.bossGroup = bossGroup;
        return this;
    }

    public EventLoopGroup getWorkerGroup() {
        return workerGroup;
    }

    public MessagingNodeContext setWorkerGroup(EventLoopGroup workerGroup) {
        this.workerGroup = workerGroup;
        return this;
    }


    public MessagingNodeContext setSegmentGroup(ExecutorService segmentGroup) {
        if (null == segmentGroup) {
            if (null == workerGroup) {
                throw new IllegalArgumentException(" segment group can't not be empty !");
            }
            this.segmentGroup = workerGroup;
        } else {
            this.segmentGroup = segmentGroup;
        }
        return this;
    }


    public void executeRunnable(Runnable runnable) {
        this.segmentGroup.execute(runnable);
    }


    private void checkState() {
        if (properties.getNodePort() == 0) {
            throw new IllegalStateException(" local node port not set ！");
        }
        if (Objects.isNull(localDestination)) {
            throw new IllegalStateException(" local destination not set !");
        }
        if (Objects.isNull(bossGroup)) {
            throw new IllegalStateException(" boss group not set !");
        }
        if (Objects.isNull(workerGroup)) {
            throw new IllegalStateException(" worker group not set! ");
        }
        if (Objects.isNull(segmentGroup)) {
            throw new IllegalStateException(" segment group not set !");
        }
    }

    public RemoteNodeMonitor getNodeMonitor() {
        return nodeMonitor;
    }

    public void start() throws Exception {
        checkState();
        //时间要求并不严格,1秒1次 64格
        if(properties.isEnableSync()){
            this.initCommandMonitor();
        }
        this.initTimer();
        this.initCommandRouter();
        this.initLocalServerNode();
        this.initNodeMonitor();
        this.initLocalServer();

    }

    private void initCommandMonitor(){
        commandMonitor = new DefaultSyncCommandMonitor();
    }

    private void initTimer(){
        globalTimer = new GlobalTimer(1,TimeUnit.SECONDS,64);
    }

    private void initCommandRouter(){
        commandRouter = new DefaultGlobalCommandRouter(this);
    }

    private void initLocalServerNode(){
        localNode = LocalNodeFactory.getNodeProxyInstance(this);
    }
    private void initNodeMonitor(){
        nodeMonitor = new DefaultLocalClientRemoteNodeMonitor(this);
        nodeMonitor.initLocalClient(bean -> {
            bean.registerForwardSlot(commandRouter);
            commandRouter.registerCommandAcceptor(localNode);
        });
    }
    private void initLocalServer() throws Exception {
        localServer = new ClusterLocalServer(this);
        localServer.addEventListener(this);
        localServer.start();
    }



    @Override
    public void serverStartup(LocalServer localServer, MessagingNodeContext context) {

        if (!this.localDestination.equals(this.properties.masterTarget())) {
            logger.info(" 当前地址:{} , 集群地址:{} ", this.localDestination, properties.masterTarget());
            localNode.registerToCluster(properties.masterTarget());
        }else {
            localNode.registerRandomNode();
        }
        if (properties.nodeMonitorEnable()) {
            long delay = properties.nodeMonitorSeconds() < 10 ? 10 : properties.nodeMonitorSeconds();
            globalTimer.newTimeout(new NodeMonitorDebug(delay, TimeUnit.SECONDS), delay, TimeUnit.SECONDS);
        }
    }

    public boolean isEnableSync(){
        return properties.isEnableSync();
    }

    @Override
    public void serverStop(LocalServer localServer) {
        globalTimer.stopTimer();
    }


    public void registerRecoverable(String methodName, Recoverable recoverable, Object target, MethodProxy methodProxy, Object[] args) {
        executeRunnable(() -> {
            RecoverableDefinition definition = new RecoverableDefinition(methodName, recoverable.intervalSeconds(), recoverable.repeat());
            globalTimer.newTimeout(new RecoverableTask(definition, target, methodProxy, args), definition.getInterval(), TimeUnit.SECONDS);
        });
    }

    public CountDownLatch registerSyncMessage(SyncCommand command) {
        Objects.requireNonNull(commandMonitor);
        return commandMonitor.monitorCommand(command);
    }

    public void releaseSyncMessage(String commandId) {
        commandMonitor.releaseMonitor(commandId);
    }

    public RemoteNodeSlot getRemoteNodeSlot() {
        return nodeMonitor;
    }

    public Future<Object> executeCallable(Callable<Object> callable) {
        return segmentGroup.submit(callable);
    }

    private class RecoverableDefinition {
        private final String methodName;
        private final int interval;
        private final int repeat;

        RecoverableDefinition(String methodName, int interval, int repeat) {
            this.methodName = methodName;
            this.interval = interval;
            this.repeat = repeat;
        }


        String getMethodName() {
            return methodName;
        }

        int getInterval() {
            return interval;
        }

        int getRepeat() {
            return repeat;
        }
    }

    private class RecoverableTask implements TimerTask {

        private final RecoverableDefinition definition;
        private final Object invokeTarget;
        private final MethodProxy methodProxy;
        private final Object[] args;
        private AtomicInteger counter = new AtomicInteger(0);

        RecoverableTask(RecoverableDefinition definition, Object invokeTarget, MethodProxy methodProxy, Object[] args) {
            this.definition = definition;
            this.invokeTarget = invokeTarget;
            this.methodProxy = methodProxy;
            this.args = args;
        }

        @Override
        public void run(Timeout timeout) throws Exception {
            try {
                if (definition.getRepeat() != -1) {
                    if (counter.get() >= definition.getRepeat()) {
                        logger.warn(" 超过重试次数，将忽略执行方法 [{}]", definition.getMethodName());
                        return;
                    }
                }
                logger.info(" {} 第 {} 遍执行 !", definition.getMethodName(), counter.get());
                methodProxy.invokeSuper(invokeTarget, args);
                logger.info(" {} 执行成功 !", definition.getMethodName());
            } catch (Throwable throwable) {
                counter.incrementAndGet();
                timeout.timer().newTimeout(this, definition.getInterval(), TimeUnit.SECONDS);
            }
        }
    }

    private class NodeMonitorDebug implements TimerTask {


        private final long delay;
        private final TimeUnit timeUnit;

        NodeMonitorDebug(long delay, TimeUnit timeUnit) {
            this.delay = delay;
            this.timeUnit = timeUnit;
        }


        @Override
        public void run(Timeout timeout) throws Exception {
            localNode.printNodes();
            //nodeMonitor.printNodes();
            timeout.timer().newTimeout(this, delay, timeUnit);
        }
    }


}
