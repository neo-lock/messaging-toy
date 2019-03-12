package com.lockdown.messaging.cluster.reactor;

import com.lockdown.messaging.cluster.Destination;

public class ChannelEvent {

    private ChannelEventType eventType;
    private Destination destination;
    private Object param;

    public ChannelEvent(ChannelEventType eventType, Destination destination) {
        this.eventType = eventType;
        this.destination = destination;
    }

    public ChannelEvent(ChannelEventType eventType, Destination destination, Object param) {
        this.eventType = eventType;
        this.destination = destination;
        this.param = param;
    }

    public ChannelEventType getEventType() {
        return eventType;
    }

    public void setEventType(ChannelEventType eventType) {
        this.eventType = eventType;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "ChannelEvent{" +
                "eventType=" + eventType +
                ", destination=" + destination +
                ", param=" + param +
                '}';
    }
}
