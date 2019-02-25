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

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;


public class MessagingNodeContext implements LocalClient,LocalServerEventListener {


    private Logger logger = LoggerFactory.getLogger(getClass());
    private final ServerDestination localDestination;
    private final MessagingProperties properties;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ExecutorService segmentGroup;
    private Class<? extends RemoteNodeMonitorClassFactory> remoteNodeMonitorClassFactory;
    private Class<? extends LocalClientFactory> localClientFactoryClass;
    private Class<? extends LocalServerFactory> localServerFactoryClass;
    private Class<? extends LocalServerNodeFactory> localServerNodeFactoryClass;
    private LocalClient localClient;
    private LocalServer localServer;
    private RemoteNodeMonitor remoteNodeMonitor;
    private GlobalTimer globalTimer;
    private LocalServerNode localServerNode;
    private ServerNodeEventHandler eventDispatcher = new ServerNodeEventHandler();


    public MessagingNodeContext(MessagingProperties properties) {
        this.properties = properties;
        this.localDestination = new ServerDestination(IPUtils.getLocalIP(), properties.getNodePort());
    }

    public void shutdownExecutor() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        if (!segmentGroup.isShutdown()) {
            segmentGroup.shutdown();
        }
    }

    public RemoteNodeMonitor getRemoteNodeMonitor() {
        return remoteNodeMonitor;
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
        if(null==segmentGroup){
            if(null==workerGroup){
                throw new IllegalArgumentException(" segment group can't not be empty !");
            }
            this.segmentGroup = workerGroup;
        }else{
            this.segmentGroup = segmentGroup;
        }
        return this;
    }



    public ServerNodeEventHandler getEventDispatcher() {
        return eventDispatcher;
    }

    public void executeRunnable(Runnable runnable){
        this.segmentGroup.execute(runnable);
    }


    public MessagingNodeContext checkState(){
        if(properties.getNodePort() == 0){
            throw new IllegalStateException(" local node port not set ！");
        }
        if(Objects.isNull(localDestination)){
            throw new IllegalStateException(" local destination not set !");
        }
        if(Objects.isNull(bossGroup)){
            throw new IllegalStateException(" boss group not set !");
        }
        if(Objects.isNull(workerGroup)){
            throw new IllegalStateException(" worker group not set! ");
        }
        if(Objects.isNull(segmentGroup)){
            throw new IllegalStateException(" segment group not set !");
        }
        if(Objects.isNull(remoteNodeMonitorClassFactory)){
            throw new IllegalStateException(RemoteNodeMonitorClassFactory.class+" not set !");
        }
        if(Objects.isNull(localClientFactoryClass)){
            throw new IllegalStateException(LocalClientFactory.class+" not set !");
        }
        if(Objects.isNull(localServerFactoryClass)){
            throw new IllegalStateException(LocalServerFactory.class+" not set !");
        }
        if(Objects.isNull(localServerNodeFactoryClass)){
            throw new IllegalStateException(LocalServerNodeFactory.class+" not set !");
        }
        return this;
    }

    @Override
    public ChannelFuture connect(ServerDestination source) {
        if(null == localClient){
            throw new IllegalStateException(" local client not set !");
        }
        return localClient.connect(source);
    }

    public MessagingNodeContext setNodeEventListenerFactoryClass(Class<? extends RemoteNodeMonitorClassFactory> clazz) {
        this.remoteNodeMonitorClassFactory = clazz;
        return this;
    }

    public MessagingNodeContext setLocalClientFactoryClass(Class<? extends LocalClientFactory> clazz) {
        this.localClientFactoryClass = clazz;
        return this;
    }

    public MessagingNodeContext setLocalServerFactoryClass(Class<? extends LocalServerFactory> clazz) {
        this.localServerFactoryClass = clazz;
        return this;
    }

    public MessagingNodeContext setLocalServerNodeFactoryClass(Class<? extends LocalServerNodeFactory> localServerNodeFactoryClass) {
        this.localServerNodeFactoryClass = localServerNodeFactoryClass;
        return this;
    }

    public GlobalTimer getGlobalTimer() {
        return globalTimer;
    }

    public void start() throws Exception {
        checkState();
        globalTimer = new GlobalTimer();
        Class.forName(remoteNodeMonitorClassFactory.getName());
        Class.forName(localClientFactoryClass.getName());
        Class.forName(localServerFactoryClass.getName());
        Class.forName(localServerNodeFactoryClass.getName());
        remoteNodeMonitor = remoteNodeMonitorClassFactory.newInstance().getInstance(this);
        localClient = localClientFactoryClass.newInstance().getInstance(this);
        localServer = localServerFactoryClass.newInstance().getInstance(this);
        eventDispatcher.setEventListener(remoteNodeMonitor);
        localServerNode = localServerNodeFactoryClass.newInstance().getInstance(this);
        localServer.addEventListener(this,remoteNodeMonitor);
        localServer.start();
    }

    @Override
    public void serverStartup(LocalServer localServer, MessagingNodeContext context) {
        logger.info(" 当前地址:{} , 集群地址:{} ",this.localDestination,properties.masterTarget());
        if(!this.localDestination.equals(this.properties.masterTarget())){
            logger.info(" 连接集群节点 {}",this.properties.masterTarget());
            localServerNode.registerToCluster(this.properties.masterTarget());
        }
        globalTimer.serverStartup(localServer,context);
        if(properties.nodeMonitorEnable()){
            long delay = properties.nodeMonitorSeconds()<10?10:properties.nodeMonitorSeconds();
            globalTimer.newTimeout(new NodeMonitorDebug(delay,TimeUnit.SECONDS),delay,TimeUnit.SECONDS);
        }

    }

    @Override
    public void serverStop(LocalServer localServer) {
        //shutdownExecutor();
        globalTimer.serverStop(localServer);
    }

    public  class NodeMonitorDebug implements TimerTask{

        private final long delay;
        private final TimeUnit unit;

        public NodeMonitorDebug(long delay, TimeUnit unit) {
            this.delay = delay;
            this.unit = unit;
        }

        @Override
        public void run(Timeout timeout) throws Exception {
            remoteNodeMonitor.printNodes();
            timeout.timer().newTimeout(this,delay,unit);
        }
    }
}
