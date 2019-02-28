package com.lockdown.messaging.actor.handler;
import com.lockdown.messaging.cluster.ServerContext;
import io.netty.channel.ChannelHandler;
import java.util.Collections;
import java.util.List;
public class ActorChannelInitializer extends AbstractChannelInitializer {


    protected ActorChannelInitializer(ServerContext serverContext) {
        super(serverContext);
    }



    @Override
    protected List<ChannelHandler> provideActorHandler() {
        return Collections.singletonList(new ActorTestHandler());
    }


}
