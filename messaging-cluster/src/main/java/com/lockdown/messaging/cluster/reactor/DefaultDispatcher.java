package com.lockdown.messaging.cluster.reactor;

import com.lockdown.messaging.cluster.channel.MessagingChannel;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class DefaultDispatcher implements Dispatcher,Runnable {



    private NodeChannelGroup nodeChannelGroup;
    private final Selector selector;
    private ExecutorService executorService;


    public DefaultDispatcher(Selector selector,NodeChannelGroup channelGroup,ExecutorService executorService) {
        this.selector = selector;
        this.nodeChannelGroup = channelGroup;
        this.executorService = executorService;
    }


    @Override
    public boolean registerChannel(MessagingChannel channel) {
        return nodeChannelGroup.addChannel(channel);
    }



    @Override
    public void run() {
        while (true){
            List<ChannelEvent> eventList = selector.select();
            eventList.forEach(channelEvent -> executorService.execute(() -> nodeChannelGroup.getChannel(channelEvent.getDestination()).handleEvent(channelEvent)));
        }
    }
}
