package com.lockdown.messaging.example.actor;

import com.lockdown.messaging.actor.ActorDestination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;



    @RequestMapping(value = "/actor/send", method = RequestMethod.POST)
    public void sendMessage(@RequestBody MessageRequest request) throws Exception {
        messageService.sendMessage(request.getAccountId(),request.getMessage());
    }


    @RequestMapping(value = "/actors",method = RequestMethod.GET)
    public Collection<ActorDestination> allActors() throws Exception{
        return messageService.allActors();
    }



    public static class MessageRequest{
        private String accountId;
        private String message;

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }


}
