package com.lockdown.messaging.core;

import java.io.Serializable;

public interface Receivable<T> {

    void receive(T message);

}
