package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.command.NodeRegister;
import com.lockdown.messaging.cluster.node.invoker.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 状态
 * <p>
 * 最新状态
 * <p>
 * 有了跟班的状态
 */
public class DefaultLocalServerNode implements LocalServerNode {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private LocalNodeCommandRouter commandRouter;
    private ServerDestination localDestination;
    private AtomicReference<ServerDestination> monitor = new AtomicReference<>();
    private AtomicReference<ServerDestination> attached = new AtomicReference<>();

    //调度算法，可以更换
    private LocalServerNodeCommandExecutor commandExecutor = new LocalServerNodeCommandExecutor();

    public DefaultLocalServerNode() {

    }

    public DefaultLocalServerNode(LocalNodeCommandRouter commandRouter, ServerDestination destination) {
        this.commandRouter = commandRouter;
        this.localDestination = destination;
        this.commandRouter.registerCommandAcceptor(this);
        this.initCommandExecutor();
    }

    public void initCommandExecutor() {
        this.commandExecutor.registerInvoker(new NodeRegisterInvoker());
        this.commandExecutor.registerInvoker(new NodeRegisterForwardInvoker());
        this.commandExecutor.registerInvoker(new NodeMonitoredInvoker());
        this.commandExecutor.registerInvoker(new NodeClosedInvoker());
    }

    public void setCommandRouter(LocalNodeCommandRouter commandRouter) {
        this.commandRouter = commandRouter;
    }

    public void setLocalDestination(ServerDestination localDestination) {
        this.localDestination = localDestination;
    }

    @Recoverable
    @Override
    public void registerToCluster(ServerDestination destination) {
        commandRouter.sendCommand(destination, new NodeRegister(this.localDestination));
    }


    @Override
    public boolean isMonitored() {
        return Objects.nonNull(monitor.get());
    }

    @Override
    public void notifyRemote(NodeCommand command, ServerDestination... ignore) {
        commandRouter.notifyCommand(command, ignore);
    }

    @Override
    public void monitor(ServerDestination destination) {
        if (Objects.nonNull(monitor.get())) {
            throw new IllegalStateException(" monitor already set !");
        }
        monitor.set(destination);
    }

    @Override
    public void attachTo(ServerDestination destination) {
        if (Objects.nonNull(attached.get())) {
            throw new IllegalStateException(" attached already set !");
        }
        attached.set(destination);
    }

    @Override
    public void sendCommand(ServerDestination target, NodeCommand command) {
        commandRouter.sendCommand(target, command);
    }


    @Override
    public boolean isAttached() {
        return Objects.nonNull(this.attached.get());
    }

    @Override
    public boolean monitorCompareAndSet(ServerDestination old, ServerDestination update) {
        return monitor.compareAndSet(old, update);
    }

    @Override
    public boolean attachedCompareAndSet(ServerDestination old, ServerDestination update) {
        return attached.compareAndSet(old, update);
    }

    @Override
    public void registerRandomNode() {
        commandRouter.sendRandomTarget(new NodeRegister(this.localDestination));
    }

    @Override
    public ServerDestination destination() {
        return this.localDestination;
    }


    @Override
    public void commandEvent(RemoteNode serverNode, NodeCommand command) {
        this.commandExecutor.executeCommand(this, serverNode, command);
    }


}
