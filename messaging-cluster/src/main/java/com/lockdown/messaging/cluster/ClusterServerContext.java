package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.node.*;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ClusterServerContext<T extends ClusterProperties> extends AbstractServerContext<T> {


    private Logger logger = LoggerFactory.getLogger(getClass());

    private NodeMessageRouter commandRouter;
    private ClusterNodeMonitor nodeMonitor;


    public ClusterServerContext(T properties) {
        super(properties);
    }

    @Override
    protected void checkProperties() {
        if(StringUtil.isNullOrEmpty(getProperties().getNodeWhiteList())){
            throw new IllegalArgumentException(" node white list can't be empty !");
        }
    }

    @Override
    protected LocalNode initLocalNode() {
        return new RecoverableLocalNodeFactory(this).getNodeInstance();
    }



    @Override
    protected void initNecessary() {
        this.nodeMonitor = new SimpleNodeMonitorFactory(this).getInstance();
        this.commandRouter = new MessageSegmentRouter(nodeMonitor, contextExecutor());
    }



    @Override
    public void shutdownContext() {
        super.shutdownContext();
        if (Objects.nonNull(this.nodeMonitor)) {
            this.nodeMonitor.shutdown();
        }
    }

    @Override
    public ClusterNodeMonitor nodeMonitor() {
        return nodeMonitor;
    }


    @Override
    public NodeMessageRouter commandRouter() {
        return commandRouter;
    }




}
