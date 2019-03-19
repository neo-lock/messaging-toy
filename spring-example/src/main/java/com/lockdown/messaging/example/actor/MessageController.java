package com.lockdown.messaging.example.actor;

import com.lockdown.messaging.example.ActorServerUtils;
import com.lockdown.messaging.example.message.JsonMessage;
import com.lockdown.messaging.example.message.TextMessage;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 测试
 */
@RestController
public class MessageController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ActorServerUtils actorServerUtils;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "message",paramType = "form",required = true)
    })
    @RequestMapping(value = "/message/notify",method = RequestMethod.POST)
    public void notifyMessage(@RequestParam String message){
        logger.info(" 开始推送消息===========》 {}",message);
        actorServerUtils.notifyMessage(new TextMessage(message));
    }




}
