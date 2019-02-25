package com.lockdown.messaging.cluster.node;
import com.lockdown.messaging.cluster.ServerDestination;

/**
 * 1.新加入的机器如何处理
 * 2.有机器断开如何处理
 */
public interface ServerNode {

    ServerDestination destination();

}
