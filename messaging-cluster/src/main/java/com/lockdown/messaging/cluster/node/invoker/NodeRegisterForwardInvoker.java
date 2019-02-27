package com.lockdown.messaging.cluster.node.invoker;

import com.alibaba.fastjson.JSON;
import com.lockdown.messaging.cluster.command.*;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.node.RemoteNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeRegisterForwardInvoker implements NodeCommandInvoker<LocalNode> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public CommandType supportType() {
        return CommandType.REGISTER_FORWARD;
    }

    @Override
    public void executeCommand(LocalNode local, RemoteNode remote, NodeCommand command) {
        NodeRegisterForward registerForward = (NodeRegisterForward) command;
        logger.info("收到监控转发{}", JSON.toJSONString(registerForward));
        if(local.isAttached()){
            local.printNodes();
            if(local.attachedCompareAndSet(registerForward.getSource(),registerForward.getTarget())){
                logger.info("当前节点成功替换依赖对象,开始进行重新注册 {}",registerForward.getTarget());
                local.registerToCluster(registerForward.getTarget());
            }else{
                logger.info("开始打个招呼");
                local.sendCommand(registerForward.getTarget(), new NodeGreeting(local.destination()));
            }
        }else{
            logger.info("当前对象没有attached,开始向{}注册",registerForward.getTarget());
            local.registerToCluster(registerForward.getTarget());
        }

    }
}
