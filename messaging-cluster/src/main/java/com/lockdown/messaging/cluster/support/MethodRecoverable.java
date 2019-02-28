package com.lockdown.messaging.cluster.support;

import net.sf.cglib.proxy.MethodProxy;

public interface MethodRecoverable {

    void registerRecoverable(String s, Recoverable recoverable, Object o, MethodProxy methodProxy, Object[] args);

}
