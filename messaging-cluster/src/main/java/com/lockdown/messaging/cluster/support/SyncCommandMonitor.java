package com.lockdown.messaging.cluster.support;

import com.lockdown.messaging.cluster.command.SyncCommand;

import java.util.concurrent.CountDownLatch;

@Deprecated
public interface SyncCommandMonitor {


    public CountDownLatch monitorCommand(SyncCommand command);


    public void releaseMonitor(String commandId);

}
