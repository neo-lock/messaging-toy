package com.lockdown.messaging.cluster.command;

import com.lockdown.messaging.cluster.exception.MessagingSerializeException;
import com.lockdown.messaging.cluster.sockethandler.ProtostuffUtils;

public interface CommandType{

    Short getType();
}
