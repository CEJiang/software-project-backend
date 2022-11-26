package software.project.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import software.project.project.component.websocket.InMessage;
import software.project.project.component.websocket.WSService;

@RestController
public class ChatController {

    @Autowired
    private WSService webSocketService;
    @PostMapping("/private-message")
    public void sendMessage(@RequestBody InMessage request){
        // webSocketService.sendMessage(request.getReceiver(), request.getMessage());
    }

    // send private message

    
}
