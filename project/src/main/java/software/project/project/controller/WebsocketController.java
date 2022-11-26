package software.project.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
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
}
