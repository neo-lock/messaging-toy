package com.lockdown.messaging.example.actor;

import com.lockdown.messaging.example.ActorServerUtils;
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

    @Autowired
    private MessageService messageService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "message",paramType = "form",required = true)
    })
    @RequestMapping(value = "/message/notify",method = RequestMethod.POST)
    public void notifyMessage(@RequestParam String message){
        actorServerUtils.notifyMessage(new TextMessage(message));
    }


    @RequestMapping(value = "/message/push",method = RequestMethod.POST)
    public void sendMessage(@RequestBody PushMessageRequest request) throws Exception {
        messageService.pushMessage(request);
    }




    public static class PushMessageRequest {
        private String receiverId;
        private String message;


        public String getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(String receiverId) {
            this.receiverId = receiverId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }




}
