package com.lockdown.messaging.cluster.channel;

import com.lockdown.messaging.cluster.reactor.ReactorContext;

import java.util.concurrent.ExecutorService;

public abstract class AbstractMessagingChannelContext implements MessagingChannelContext {


    volatile AbstractMessagingChannelContext next;
    volatile AbstractMessagingChannelContext prev;
    private final ExecutorService executorService;
    private final ReactorContext reactorContext;

    AbstractMessagingChannelContext(ExecutorService executorService, ReactorContext reactorContext) {
        this.executorService = executorService;
        this.reactorContext = reactorContext;
    }

    protected ReactorContext getReactorContext() {
        return reactorContext;
    }


    @Override
    public void fireChannelRegistered() {
        executorService.execute(() -> findNext().invokeChannelRegistered());

    }

    @Override
    public void fireChannelReceived(Object message) {
        executorService.execute(() -> findNext().invokeChannelReceived(message));

    }

    @Override
    public void fireChannelClosed() {
        executorService.execute(() -> findNext().invokeChannelClosed());
    }

    @Override
    public void fireExceptionCaught(Throwable throwable) {
        executorService.execute(() -> findNext().invokeChannelExceptionCaught(throwable));
    }

    private void invokeChannelExceptionCaught(Throwable throwable){
        (this.handler()).exceptionCaught(this,throwable);
    }

    private void invokeChannelClosed(){
        (this.handler()).channelClosed(this);
    }

    private void invokeChannelRegistered(){
        try{
            (this.handler()).channelRegistered(this);
        }catch (Throwable throwable){
            fireExceptionCaught(throwable);
        }

    }

    private void invokeChannelReceived(Object message){
        try{
            (this.handler()).channelReceived(this,message);
        }catch (Throwable t){
            fireExceptionCaught(t);
        }

    }

    private AbstractMessagingChannelContext findNext(){
        return this.next;
    }
}
