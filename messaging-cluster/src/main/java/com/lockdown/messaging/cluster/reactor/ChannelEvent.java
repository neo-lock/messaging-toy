package com.lockdown.messaging.cluster.reactor;

import com.lockdown.messaging.cluster.Destination;

public final class ChannelEvent {

    private Enum<?> channelType;
    private ChannelEventType eventType;
    private Destination destination;
    private Object param;
    private EventSource eventSource;


    public ChannelEvent() {

    }

    public ChannelEvent(Enum<?> channelType, ChannelEventType eventType, Object param) {
        this(channelType, eventType, null, param);
    }

    public ChannelEvent(Enum<?> channelType, ChannelEventType eventType, Destination destination) {
        this(channelType, eventType, destination, null);
    }

    public ChannelEvent(Enum<?> channelType, ChannelEventType eventType, Destination destination, Object param) {
        this(null, channelType, eventType, destination, param);
    }

    public ChannelEvent(EventSource source, Enum<?> channelType, ChannelEventType eventType, Object param) {
        this(source, channelType, eventType, null, param);
    }

    public ChannelEvent(EventSource source, Enum<?> channelType, ChannelEventType eventType, Destination destination) {
        this(source, channelType, eventType, destination, null);
    }

    public ChannelEvent(EventSource source, Enum<?> channelType, ChannelEventType eventType, Destination destination, Object param) {
        if (null != source) {
            this.eventSource = new DefaultEventSource(source.channelType(), source.destination());
        }
        this.channelType = channelType;
        this.eventType = eventType;
        this.param = param;
        this.destination = destination;
    }

    public EventSource getEventSource() {
        return eventSource;
    }

    public void setEventSource(EventSource eventSource) {
        this.eventSource = eventSource;
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

    private class DefaultEventSource implements EventSource {

        private Enum<?> channelType;
        private Destination destination;

        public DefaultEventSource(Enum<?> channelType, Destination destination) {
            this.channelType = channelType;
            this.destination = destination;
        }

        @Override
        public Enum<?> channelType() {
            return channelType;
        }

        @Override
        public Destination destination() {
            return destination;
        }
    }
}
