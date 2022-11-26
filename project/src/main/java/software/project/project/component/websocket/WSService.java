package software.project.project.component.websocket;

import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WSService {
    
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WSService(SimpMessagingTemplate messagingTemplate){
        this.messagingTemplate = messagingTemplate;
    }
    
    // public void sendMessage(String to, String message){
    //     Map<String, Object> messageMap = new HashMap<>();
    //     messageMap.put("message", message);
    //     messagingTemplate.convertAndSend("/private-message", messageMap);
    // }

    public void sendChatMessage(InMessage message) {
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("message", message.getMessage());
        System.out.println(message.getMessage());
        messagingTemplate.convertAndSend("/chat/single/" + message.getReceiver(), messageMap);
    }
}
