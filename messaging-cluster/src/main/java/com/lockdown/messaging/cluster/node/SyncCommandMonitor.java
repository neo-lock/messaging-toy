package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.command.SyncCommand;

import java.util.concurrent.CountDownLatch;

public interface SyncCommandMonitor {


    public CountDownLatch monitorCommand(SyncCommand command);


    public void releaseMonitor(String commandId);

}
