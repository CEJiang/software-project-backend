package software.project.project.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import software.project.project.component.chat.Message;
import software.project.project.component.websocket.WSService;

@Controller
public class WebsocketController {
    @Autowired
    private WSService ws;
    
    @MessageMapping("/ptp/single/chat")  // /auth/
    public void privateMessage(Message message) throws JsonMappingException, JsonProcessingException {
        ws.sendChatMessage(message);
    }

    @MessageMapping("/chat/addUser/{userID}")
    public void addUser(@PathVariable("userID") String userID, SimpMessageHeaderAccessor simpMessageHeaderAccessor){
        System.out.println(simpMessageHeaderAccessor.getSessionId());
        System.out.println(userID);
        ws.addUser(userID, simpMessageHeaderAccessor.getSessionId());
    }
}
