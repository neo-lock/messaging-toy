package com.lockdown.messaging.cluster.support;

import com.lockdown.messaging.cluster.command.SyncCommand;
import com.lockdown.messaging.cluster.support.SyncCommandMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class DefaultSyncCommandMonitor implements SyncCommandMonitor {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private Map<String, CountDownLatch> commandContext = new ConcurrentHashMap<>();


    @Override
    public CountDownLatch monitorCommand(SyncCommand command) {
        if (commandContext.containsKey(command.getCommandId())) {
            throw new IllegalStateException(" command exits !");
        }
        CountDownLatch countDownLatch = new CountDownLatch(1);
        commandContext.put(command.getCommandId(), countDownLatch);
        logger.info(" 监控同步消息 {}", command.getCommandId());
        return countDownLatch;
    }

    @Override
    public void releaseMonitor(String commandId) {
        CountDownLatch countDownLatch = commandContext.remove(commandId);
        if (null != countDownLatch) {
            logger.info("释放同步消息 {}", commandId);
            countDownLatch.countDown();
        }
    }
}
