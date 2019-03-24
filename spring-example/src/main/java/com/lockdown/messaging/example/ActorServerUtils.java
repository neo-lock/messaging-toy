package com.lockdown.messaging.example;
import com.lockdown.messaging.actor.Actor;
import com.lockdown.messaging.actor.ActorDestination;

public interface ActorServerUtils {

    /**
     * notify message
     * @param message
     */
    public void notifyMessage(Object message);

    /**
     * 发送消息给指定的destination
     * @param destination
     * @param message
     */
    public void pushMessage(ActorDestination destination, Object message);

    /**
     * 获取本地的actor
     * @param destination
     * @return
     */
    public Actor getLocalActor(ActorDestination destination);

}
