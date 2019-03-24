package com.lockdown.messaging.example.actor;

public interface MessageService {


    public void pushMessage(MessageController.PushMessageRequest request) throws Exception;

}
