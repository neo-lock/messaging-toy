package com.lockdown.messaging.cluster.command;

public abstract class AbstractCommandCodec implements CommandCodec {


    protected void checkContent(byte[] content){
        if (null == content || content.length == 0) {
            throw new IllegalArgumentException(" content can not be empty !");
        }
    }

}
