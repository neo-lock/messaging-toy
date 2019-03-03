package com.lockdown.messaging.actor.framework;

import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.actor.ActorNodeType;
import com.lockdown.messaging.actor.ActorServerContext;
import com.lockdown.messaging.actor.ClusterActorServerContext;
import com.lockdown.messaging.actor.command.NodeActorCommand;
import com.lockdown.messaging.actor.exception.ActorNotFoundException;
import com.lockdown.messaging.cluster.framwork.ChannelSlot;
import com.lockdown.messaging.cluster.node.RemoteNode;
import com.lockdown.messaging.cluster.node.RemoteNodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ClusterActorMessageRouter implements ActorMessageRouter {


    private final ActorMonitor actorMonitor;
    private ActorMessageCodec<?> messageCodec;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ClusterActorServerContext serverContext;
    private ChannelMessageHandler messageHandler = new ActorChannelMessageHandler();

    public ClusterActorMessageRouter(ClusterActorServerContext serverContext) {
        this.serverContext = serverContext;
        this.actorMonitor = serverContext.actorMonitor();
        this.actorMonitor.registerAcceptor(this);
        this.initActorMessageCodec();
    }

    private void initActorMessageCodec(){
        try {
            Class<?> clazz = Class.forName(serverContext.getProperties().getActorCodecClassName());
            messageCodec = (ActorMessageCodec<?>) clazz.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }


    private void routeMessage(ActorDestination receiver, ActorDestination sender, Object message) {
        Actor actor = actorMonitor.findByDestination(receiver);
        if (Objects.isNull(actor)) {
            throw new ActorNotFoundException(receiver + " not found !");
        }
        serverContext.contextExecutor().getSegment().execute(() -> actor.receivedMessage(sender, message));
    }


    @Override
    public void acceptedMessage(ChannelSlot channelSlot, Object message) {
        messageHandler.messageProcess(channelSlot, message);
    }

    @Override
    public void sendMessage(ActorDestination destination, Object message) {
        logger.info(" 发送消息 {}",message);
    }


    private interface ChannelMessageHandler {

        public void messageProcess(ChannelSlot channelSlot, Object message);
    }

    private interface ChannelTypeMessageHandler extends ChannelMessageHandler {
        Enum<?> supportedType();
    }

    private class ActorChannelMessageHandler implements ChannelMessageHandler {

        private Map<Enum<?>, ChannelTypeMessageHandler> handlerContext = new HashMap<>();

        public ActorChannelMessageHandler() {
            addChannelTypeMessageHandler(new ActorMessageHandler());
            addChannelTypeMessageHandler(new RemoteNodeMessageHandler());
        }

        private void addChannelTypeMessageHandler(ChannelTypeMessageHandler messageHandler) {
            this.handlerContext.putIfAbsent(messageHandler.supportedType(), messageHandler);
        }

        @Override
        public void messageProcess(ChannelSlot channelSlot, Object message) {
            if (!handlerContext.containsKey(channelSlot.slotType())) {
                throw new IllegalStateException(" can't supported current channel type " + channelSlot.slotType());
            }
            handlerContext.get(channelSlot.slotType()).messageProcess(channelSlot, message);
        }
    }

    private class ActorMessageHandler implements ChannelTypeMessageHandler {

        @Override
        public Enum<?> supportedType() {
            return ActorNodeType.ACTOR_NODE;
        }

        @Override
        public void messageProcess(ChannelSlot channelSlot, Object message) {
            if (!Actor.class.isAssignableFrom(channelSlot.getClass())) {
                throw new IllegalStateException(" can't supported current type " + channelSlot.getClass());
            }
            Actor actor = (Actor) channelSlot;
            try {
                serverContext.contextExecutor().executeRunnable(() -> actor.receivedMessage(message));
            } catch (Exception ex) {
                logger.warn(" actor received message execute exception {}", ex.getMessage());
                try {
                    serverContext.contextExecutor().executeRunnable(() -> actor.exceptionCaughtEvent(ex));
                } catch (Exception error) {
                    error.printStackTrace();
                    //ignore
                }
            }

        }
    }

    private class RemoteNodeMessageHandler implements ChannelTypeMessageHandler {

        @Override
        public Enum<?> supportedType() {
            return RemoteNodeType.REMOTE_NODE;
        }

        @Override
        public void messageProcess(ChannelSlot channelSlot, Object message) {
            if (!RemoteNode.class.isAssignableFrom(channelSlot.getClass())) {
                throw new IllegalStateException(" can't supported current type " + channelSlot.getClass());
            }
            if (!(message instanceof NodeActorCommand)) {
                throw new IllegalStateException(" can't supported current message type " + message.getClass());
            }
            RemoteNode remoteNode = (RemoteNode) channelSlot;
            NodeActorCommand actorCommand = (NodeActorCommand) message;
            handleNodeCommand(remoteNode, actorCommand);
        }


        private void handleNodeCommand(RemoteNode node, NodeActorCommand actorCommand) {
            ActorDestination receiver = new ActorDestination(serverContext.localDestination(), actorCommand.getReceiverChannel());
            ActorDestination sender = new ActorDestination(node.destination(), actorCommand.getSenderChannel());
            Object object = messageCodec.decode(actorCommand.getContent());
            routeMessage(receiver, sender, object);
        }

    }


}
