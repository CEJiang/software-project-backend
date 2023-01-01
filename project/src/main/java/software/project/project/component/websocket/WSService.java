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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.project.project.component.chat.Message;
import software.project.project.component.chat.MessageService;
import software.project.project.component.redis.RedisService;
import software.project.project.component.resume.Resume;
import software.project.project.component.resume.ResumeService;

@Service
public class WSService {

    @Autowired
    private RedisService redisService;
    
    @Autowired
    private MessageService messageService;

    @Autowired
    private ResumeService resumeService;

    private final SimpMessagingTemplate messagingTemplate;
    private Map<String, String> online = new ConcurrentHashMap<>();

    public WSService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handlerWebSocketConnectListener(SessionConnectEvent sessionConnectEvent) {
    }

    @EventListener
    public void handlerWebSocketDisConnectListener(SessionDisconnectEvent sessionDisconnectEvent) {
        System.out.println("SessionId = " + sessionDisconnectEvent.getSessionId());
        online.remove(sessionDisconnectEvent.getSessionId());
        System.out.println(online);

    }

    public void sendChatMessage(Message message) throws JsonMappingException, JsonProcessingException {
        System.out.println(message.toString());
        
        if(message.getType().equals("resume")){
            ObjectMapper mapper = new ObjectMapper();
            ResumeType resumeType = mapper.readValue(message.getMessage(), ResumeType.class);
            Resume resume = resumeService.getResume(resumeType.getUserID(), resumeType.getCreateTime());
            message.setMessage(mapper.writeValueAsString(resume));
        }

        if(online.containsValue(message.getReceiver())){
            messagingTemplate.convertAndSend("/chat/single/" + message.getReceiver(), message);
        }
        else{
            // store in redis
            redisService.setChatDataRedis(message);
        }
        // store in database
        messageService.saveMessage(message);
    }

    public void addUser(String userID, String sessionID) {
        online.put(sessionID, userID);
        System.out.println(online);
        // sendAll(userID);
    }

    public void sendAll(String userID) {
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("userID", userID);
        System.out.println(userID);
        messagingTemplate.convertAndSend("/chat/notice", messageMap);
    }
}
