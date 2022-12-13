package software.project.project.component.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@Service
public class WSService {
    
    private final SimpMessagingTemplate messagingTemplate;
    private Map<String, String> online = new ConcurrentHashMap<>();
    @Autowired
    public WSService(SimpMessagingTemplate messagingTemplate){
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handlerWebSocketConnectListener(SessionConnectEvent sessionConnectEvent){
    }

    @EventListener
    public void handlerWebSocketDisConnectListener(SessionDisconnectEvent sessionDisconnectEvent){
        System.out.println("SessionId = " + sessionDisconnectEvent.getSessionId());
        online.remove(sessionDisconnectEvent.getSessionId());
        System.out.println(online);
        
    }
    public void sendChatMessage(InMessage message) {
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("message", message.getMessage());
        System.out.println(message.getMessage());
        messagingTemplate.convertAndSend("/chat/single/" + message.getReceiver(), messageMap);
    }

    public void addUser(String userID, String sessionID){
        online.put(sessionID, userID);
        System.out.println(online);
        sendAll(userID);
    }

    public void sendAll(String userID){
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("userID", userID);
        System.out.println(userID);
        messagingTemplate.convertAndSend("/chat/notice", messageMap);
    }
}
