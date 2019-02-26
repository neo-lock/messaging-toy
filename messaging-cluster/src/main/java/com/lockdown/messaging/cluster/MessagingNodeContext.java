package com.lockdown.messaging.cluster;

import com.alibaba.fastjson.JSON;
import com.lockdown.messaging.cluster.event.LocalServerEventListener;
import com.lockdown.messaging.cluster.node.*;
import com.lockdown.messaging.cluster.utils.GlobalTimer;
import com.lockdown.messaging.cluster.utils.IPUtils;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;


public class MessagingNodeContext implements LocalServerEventListener {


    private final ServerDestination localDestination;
    private final MessagingProperties properties;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ExecutorService segmentGroup;
    private GlobalTimer globalTimer;
    private LocalServerNode localServerNode;
    private LocalClientRemoteNodeMonitor nodeMonitor;
    private GlobalCommandRouter commandRouter;
    private LocalServer localServer;



    public MessagingNodeContext(MessagingProperties properties) {
        this.properties = properties;
        this.localDestination = new ServerDestination(IPUtils.getLocalIP(), properties.getNodePort());
    }

    public void shutdownExecutor() {
        if(Objects.nonNull(bossGroup)&&!bossGroup.isShutdown()){
            bossGroup.shutdownGracefully();
        }
        if(Objects.nonNull(workerGroup)&&!workerGroup.isShutdown()){
            workerGroup.shutdownGracefully();
        }
        if(Objects.nonNull(segmentGroup)&&!segmentGroup.isShutdown()){
            segmentGroup.shutdown();
        }
    }



    public ServerDestination getLocalDestination() {
        return localDestination;
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

    public LocalClientRemoteNodeMonitor getNodeMonitor() {
        return nodeMonitor;
    }

    public void start() throws Exception {
        checkState();
        globalTimer = new GlobalTimer();
        commandRouter = new DefaultGlobalCommandRouter(this);
        localServerNode = new DefaultLocalServerNode(commandRouter,this.getLocalDestination());
        nodeMonitor = new DefaultRemoteNodeMonitor(this);
        nodeMonitor.initLocalClient(bean -> {
            bean.registerForwardSlot(commandRouter);
            commandRouter.registerCommandAcceptor(localServerNode);
        });
        localServer = new ClusterLocalServer(this);
        localServer.addEventListener(this);
        localServer.start();
    }

    @Override
    public void serverStartup(LocalServer localServer, MessagingNodeContext context) {
        if (!this.localDestination.equals(this.properties.masterTarget())) {
            logger.info(" 当前地址:{} , 集群地址:{} ", this.localDestination, properties.masterTarget());
            localServerNode.registerToCluster(properties.masterTarget());
        }
        globalTimer.serverStartup(localServer, context);
        if (properties.nodeMonitorEnable()) {
            long delay = properties.nodeMonitorSeconds() < 10 ? 10 : properties.nodeMonitorSeconds();
            globalTimer.newTimeout(new NodeMonitorDebug(delay, TimeUnit.SECONDS), delay, TimeUnit.SECONDS);
        }
    }

    @Override
    public void serverStop(LocalServer localServer) {
        globalTimer.serverStop(localServer);
    }

    private class NodeMonitorDebug implements TimerTask{

        private final long delay;
        private final TimeUnit timeUnit;

        NodeMonitorDebug(long delay, TimeUnit timeUnit) {
            this.delay = delay;
            this.timeUnit = timeUnit;
        }

        @Override
        public void run(Timeout timeout) throws Exception {
            nodeMonitor.printNodes();
            timeout.timer().newTimeout(this,delay,timeUnit);
        }
    }


}
