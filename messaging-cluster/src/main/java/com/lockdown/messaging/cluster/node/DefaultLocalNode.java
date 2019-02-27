package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.command.NodeRegister;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.node.invoker.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Objects;

/**
 * 状态
 * <p>
 * 最新状态
 * <p>
 * 有了跟班的状态
 */
public class DefaultLocalNode implements LocalNode {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final LocalNodeCommandRouter commandRouter;
    private final ServerDestination localDestination;
    private volatile ServerDestination monitor = null;
    private volatile ServerDestination attached = null;
    private final Object lock = new Object();

    //调度算法，可以更换
    private LocalServerNodeCommandExecutor commandExecutor = new LocalServerNodeCommandExecutor();


    public DefaultLocalNode(LocalNodeCommandRouter commandRouter, ServerDestination destination) {
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


    @Recoverable(repeat = -1)
    @Override
    public void registerToCluster(ServerDestination destination) {
        commandRouter.sendCommand(destination, new NodeRegister(this.localDestination));
    }


    @Override
    public boolean isMonitored() {
        return Objects.nonNull(monitor);
    }

    @Override
    public void notifyRemote(SourceNodeCommand command, ServerDestination... ignore) {
        commandRouter.notifyCommand(command, ignore);
    }

    @Override
    public void monitor(ServerDestination destination) {
        synchronized (lock){
            if (Objects.nonNull(monitor)) {
                throw new IllegalStateException(" monitor already set !");
            }
            monitor = destination;
        }
    }

    @Override
    public void attachTo(ServerDestination destination) {
        synchronized (lock){
            if (Objects.nonNull(attached)) {
                throw new IllegalStateException(" attached already set !");
            }
            attached = destination;
        }
    }

    @Override
    public void sendCommand(ServerDestination target, SourceNodeCommand command) {
        commandRouter.sendCommand(target, command);
    }


    @Override
    public boolean isAttached() {
        return Objects.nonNull(attached);
    }

    @Override
    public  boolean monitorCompareAndSet(ServerDestination old, ServerDestination update) {
        boolean result = false;
        synchronized (lock){
            if(Objects.isNull(old)){
                if(monitor == null){
                    monitor = update;
                    result = true;
                }
            }else{
                if(old.equals(monitor)){
                    monitor = update;
                    result = true;
                }
            }
        }
        return result;
    }

    @Override
    public boolean attachedCompareAndSet(ServerDestination old, ServerDestination update) {
        boolean result = false;
        synchronized (lock){
            if(Objects.isNull(old)){
                if(attached == null){
                    attached = update;
                    result = true;
                }
            }else{
                if(old.equals(attached)){
                    attached = update;
                    result = true;
                }
            }
        }
        return result;
    }

    @Recoverable(repeat = -1)
    @Override
    public void registerRandomNode() {
        commandRouter.sendRandomTarget(new NodeRegister(this.localDestination));
    }

    @Override
    public void printNodes() {
        logger.info("Local[{}],Monitor[{}],Attached[{}]",localDestination,monitor,attached);
    }

    @Override
    public ServerDestination forceMonitor(ServerDestination destination) {
        ServerDestination result = null;
        synchronized(lock){
            result = monitor;
            monitor = destination;
        }
        return result;
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
