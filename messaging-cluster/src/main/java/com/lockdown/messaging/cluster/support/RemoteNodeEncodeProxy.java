package com.lockdown.messaging.cluster.support;

import com.lockdown.messaging.cluster.command.NodeCommand;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class RemoteNodeEncodeProxy implements MethodInterceptor {


    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        NodeCommand command = (NodeCommand) objects[0];
        ByteBuf byteBuf = Unpooled.buffer();
        byte[] content = command.type().commandToBytes(command);
        byteBuf.writeInt(NodeCommand.BASE_LENGTH + content.length);
        byteBuf.writeShort(command.type().getType());
        if (content.length > 0) {
            byteBuf.writeBytes(content);
        }
        objects[0] = byteBuf;
        Object result = methodProxy.invokeSuper(o, objects);
        byteBuf.release();
        ReferenceCountUtil.release(byteBuf);
        return result;
    }
}
