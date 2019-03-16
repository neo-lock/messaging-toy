package com.lockdown.messaging.cluster.reactor;

import com.lockdown.messaging.cluster.Destination;

public class ChannelEvent {

    private Enum<?> channelType;
    private ChannelEventType eventType;
    private Destination destination;
    private Object param;

    public ChannelEvent() {

    }

    public ChannelEvent(Enum<?> channelType, ChannelEventType eventType, Object param) {
        this.channelType = channelType;
        this.eventType = eventType;
        this.param = param;
    }

    public ChannelEvent(Enum<?> channelType, ChannelEventType eventType, Destination destination) {
        this.channelType = channelType;
        this.eventType = eventType;
        this.destination = destination;
    }

    public ChannelEvent(Enum<?> channelType, ChannelEventType eventType, Destination destination, Object param) {
        this(channelType, eventType, destination);
        this.param = param;
    }

    public Enum<?> getChannelType() {
        return channelType;
    }

    public void setChannelType(Enum<?> channelType) {
        this.channelType = channelType;
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
                "channelType=" + channelType +
                ", eventType=" + eventType +
                ", destination=" + destination +
                ", param=" + param +
                '}';
    }
}
