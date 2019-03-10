package com.lockdown.messaging.cluster.reactor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DefaultSelector implements Selector {

    private BlockingQueue<ChannelEvent> events = new LinkedBlockingQueue<>();
    private final Object lock = new Object();


    @Override
    public void addEvent(ChannelEvent event) {
        boolean result = events.offer(event);
        if(result){
            synchronized (lock){
                lock.notify();
            }

        }
    }

    @Override
    public List<ChannelEvent> select() {
        return select(10);
    }

    private List<ChannelEvent> select(long timeout){
        if(timeout>0){
            synchronized (lock){
                if(events.isEmpty()){
                    try {
                        lock.wait(timeout);
                    } catch (InterruptedException e) {
                        //ignore
                    }
                }
            }
        }
        List<ChannelEvent> eventList = new ArrayList<>();
        events.drainTo(eventList);
        return eventList;
    }
}
