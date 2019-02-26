package com.lockdown.messaging.cluster.node;

public interface InitializedCallback<T> {


    public void initialized(T bean);

}
