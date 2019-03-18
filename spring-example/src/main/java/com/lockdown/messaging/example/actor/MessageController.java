package com.lockdown.messaging.example.actor;

import com.lockdown.messaging.example.ActorServerUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 测试
 */
@RestController
public class MessageController {


    @Autowired
    private ActorServerUtils actorServerUtils;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "message",paramType = "form",required = true)
    })
    @RequestMapping(value = "/message/notify",method = RequestMethod.POST)
    public void notifyMessage(@RequestParam String message){
        actorServerUtils.notifyMessage(message);
    }




}
