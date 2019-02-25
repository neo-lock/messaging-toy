package com.lockdown.messaging.core;

public interface Writable<T> {

    void write(T message);

}
