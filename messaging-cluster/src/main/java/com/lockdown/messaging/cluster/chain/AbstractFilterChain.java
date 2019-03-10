package com.lockdown.messaging.cluster.chain;
import com.lockdown.messaging.cluster.framwork.ChannelSlot;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class AbstractFilterChain implements Chain {

    private List<Filter> filters = new ArrayList<>();
    private ThreadLocal<AtomicInteger> filterIndex = ThreadLocal.withInitial(() -> new AtomicInteger(0));

    private Filter next(){
        int index = filterIndex.get().getAndAdd(1);
        if(index<filters.size()){
            return filters.get(index);
        }
        filterIndex.remove();
        return null;
    }

    @Override
    public  void addFilter(Filter filter) {
        this.filters.add(filter);
    }

    @Override
    public void fireReceivedMessage(ChannelSlot target, Object message) {
        Filter handler = next();
        if(null!=handler){
            try{
                handler.receivedMessage(this,target,message);
            }catch (Throwable throwable){
                filterIndex.remove();
                fireExceptionCaught(target,throwable);
            }
        }
    }

    @Override
    public void fireExceptionCaught(ChannelSlot target, Throwable throwable) {
        Filter handler = next();
        if(null!=handler){
            handler.exceptionCaught(this,target,throwable);
        }
    }

    @Override
    public void fireClose(ChannelSlot target, Reason reason) {
        Filter handler = next();
        if(null!=handler){
            handler.close(this,target,reason);
        }
    }

    @Override
    public void fireInactive(ChannelSlot target) {
        Filter handler = next();
        if(null!=handler){
            handler.inactive(this,target);
        }
    }

    @Override
    public void inactive(Chain chain, ChannelSlot target) {
        this.fireInactive(target);
    }

    @Override
    public void receivedMessage(Chain chain, ChannelSlot target, Object message) {
        this.fireReceivedMessage(target,message);
    }

    @Override
    public void exceptionCaught(Chain chain, ChannelSlot target, Throwable throwable) {
        this.fireExceptionCaught(target,throwable);
    }

    @Override
    public void close(Chain chain, ChannelSlot target, Reason reason) {
        this.fireClose(target,reason);
    }


//    @Override
//    public void executeInactive(ChannelSlot target) {
//        filterIndex.remove();
//        getExecutorService().execute(()->fireInactive(target));
//    }
//
//    @Override
//    public void executeReceivedMessage(ChannelSlot target, Object message) {
//        getExecutorService().execute(()->{
//            filterIndex.remove();
//            fireReceivedMessage(target,message);
//        });
//    }
//
//    @Override
//    public void executeExceptionCaught(ChannelSlot target, Throwable throwable) {
//        getExecutorService().execute(()->{
//            filterIndex.remove();
//            fireExceptionCaught(target,throwable);
//        });
//    }
//
//    @Override
//    public void executeClose(ChannelSlot target, Reason reason) {
//        getExecutorService().execute(()->{
//            filterIndex.remove();
//            fireClose(target,reason == null?Reason.EMPTY:reason);
//        });
//    }
}
