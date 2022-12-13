package software.project.project.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import software.project.project.component.websocket.InMessage;
import software.project.project.component.websocket.WSService;

@Controller
public class WebsocketController {
    @Autowired
    private WSService ws;
    
    @MessageMapping("/ptp/single/chat")  // /auth/
    public void privateMessage(InMessage message) {
        ws.sendChatMessage(message);
    }

    @MessageMapping("/chat/addUser")
    public void addUser(temp request, SimpMessageHeaderAccessor simpMessageHeaderAccessor){
        System.out.println(simpMessageHeaderAccessor.getSessionId());
        System.out.println(request.getUserID());
        ws.addUser(request.getUserID(), simpMessageHeaderAccessor.getSessionId());
    }
}
