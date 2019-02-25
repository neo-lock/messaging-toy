package com.lockdown.messaging.cluster.node;


import com.alibaba.fastjson.JSON;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.command.NodeRegister;
import com.lockdown.messaging.cluster.node.invoker.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 状态
 * <p>
 * 最新状态
 * <p>
 * 有了跟班的状态
 */
public class DefaultLocalServerNode implements LocalServerNode, CommandAcceptor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private AtomicReference<ServerDestination> monitor = new AtomicReference<>();
    private AtomicReference<ServerDestination> attached = new AtomicReference<>();
    private RemoteNodeMonitor remoteNodeMonitor;
    private ServerDestination localDestination;
    private LocalServerNodeCommandExecutor commandExecutor = new LocalServerNodeCommandExecutor();

    DefaultLocalServerNode(RemoteNodeMonitor remoteNodeMonitor, ServerDestination destination) {
        this.remoteNodeMonitor = remoteNodeMonitor;
        this.remoteNodeMonitor.registerCommandHandler(this);
        this.localDestination = destination;
        this.initCommandExecutor();
    }

    private void initCommandExecutor() {
        this.commandExecutor.registerInvoker(new NodeRegisterInvoker());
        this.commandExecutor.registerInvoker(new NodeRegisterForwardInvoker());
        this.commandExecutor.registerInvoker(new NodeMonitoredInvoker());
        this.commandExecutor.registerInvoker(new NodeClosedInvoker());
    }

    @Override
    public void registerToCluster(ServerDestination destination) {
        RemoteServerNode remoteServerNode = remoteNodeMonitor.getRemoteNode(destination);
        remoteServerNode.sendCommand(new NodeRegister(this.localDestination));
    }


    @Override
    public boolean isMonitored() {
        return Objects.nonNull(monitor.get());
    }

    @Override
    public void notifyRemote(NodeCommand command, ServerDestination... ignore) {
        Set<ServerDestination> ignoreSet = new HashSet<>();
        if (null != ignore && ignore.length > 0) {
            ignoreSet.addAll(Arrays.asList(ignore));
        }
        remoteNodeMonitor.remoteNodes().forEach(remoteServerNode -> {
            if (ignoreSet.contains(remoteServerNode.destination())) {
                return;
            }
            remoteServerNode.sendCommand(command);
        });
    }

    @Override
    public void monitor(ServerDestination destination) {
        if (Objects.nonNull(monitor.get())) {
            throw new IllegalStateException(" monitor already set !");
        }
        logger.info(" 开始监控节点 {}", destination);
        monitor.set(destination);
    }

    @Override
    public void attachTo(ServerDestination destination) {
        if (Objects.nonNull(attached.get())) {
            throw new IllegalStateException(" attached already set !");
        }
        logger.info(" 被节点 {} 监控", destination);
        attached.set(destination);
    }

    @Override
    public void sendCommand(ServerDestination target, NodeCommand command) {
        RemoteServerNode remoteServerNode = remoteNodeMonitor.getRemoteNode(target);
        logger.info(" send command : {}{} to destination {}", command.getClass(), JSON.toJSONString(command), JSON.toJSONString(target));
        remoteServerNode.sendCommand(command);
    }



    @Override
    public boolean isAttached() {
        return Objects.nonNull(this.attached.get());
    }

    @Override
    public boolean monitorCompareAndSet(ServerDestination old, ServerDestination update) {
        return monitor.compareAndSet(old,update);
    }

    @Override
    public boolean attachedCompareAndSet(ServerDestination old, ServerDestination update) {
        return attached.compareAndSet(old,update);
    }

    @Override
    public void registerRandomNode() {
        RemoteServerNode node = remoteNodeMonitor.randomNode();
        if(Objects.nonNull(node)){
            sendCommand(node.destination(),new NodeRegister(this.localDestination));
        }
    }

    @Override
    public ServerDestination destination() {
        return this.localDestination;
    }


    @Override
    public void commandEvent(RemoteServerNode serverNode, NodeCommand command) {
        this.commandExecutor.executeCommand(this, serverNode, command);
    }


}
