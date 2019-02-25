package com.lockdown.messaging.core;

import com.lockdown.messaging.core.event.MessagingServerEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class MessagingServerManager {

    private MessagingServer messagingServer;


    private Logger logger = LoggerFactory.getLogger(getClass());

    public MessagingServerManager(){

        messagingServer = new DefaultMessagingServer(new MessagingServerEventListener() {
            @Override
            public void serverStarted(MessagingServer messagingServer) throws InterruptedException {
//                logger.info(" server start !");
//                try {
//                    TimeUnit.SECONDS.sleep(5);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("准备关闭!");
//                messagingServer.stop();
            }
        });

    }


    public void start(){
        messagingServer.start();
    }

    public static void main(String[] args){
        new MessagingServerManager().start();
    }

}
