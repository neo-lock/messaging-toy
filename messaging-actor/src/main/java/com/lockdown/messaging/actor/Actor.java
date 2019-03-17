package com.lockdown.messaging.actor;

/**
 * 一个角色，相当于一个业务上的socket连接
 */
public interface Actor {

    /**
     * 表示其他服务器发过来的消息
     * @param destination
     * @param message
     */
    void receivedMessage(ActorDestination destination, Object message);


    /**
     * 表示本地消息
     * @param message
     */
    void receivedMessage(Object message);

    /**
     * 发送消息
     * @param destination
     * @param message
     */
    void writeMessage(ActorDestination destination,Object message);


    /**
     * 发送消息
     * @param message
     */
    void writeMessage(Object message);


    /**
     * 当前连接被关闭
     */
    void closedEvent();


}
