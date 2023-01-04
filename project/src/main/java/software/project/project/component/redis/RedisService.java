package software.project.project.component.redis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.project.project.component.chat.Message;
import software.project.project.component.member.MemberAccount;

@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void setEmailRedis(String email, String verifyCode){
        redisTemplate.opsForValue().set(email, verifyCode, 5, TimeUnit.MINUTES);
    }

    public String getEmailRedis(String email){
        return redisTemplate.opsForValue().get(email);
    }
    
    public void deleteEmailRedis(String email){
        redisTemplate.delete(email);
    }

    public void setMemberAccountRedis(MemberAccount memberAccount) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(memberAccount);
            redisTemplate.opsForValue().set(memberAccount.getUserID(), json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public MemberAccount getMemberAccountRedis(String userID){
        ObjectMapper mapper = new ObjectMapper();
        String json = redisTemplate.opsForValue().get(userID);
        try {
            MemberAccount memberAccount = mapper.readValue(json, MemberAccount.class);
            return memberAccount;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setChatDataRedis(Message message) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Message> messageList = ((existRedis("message-" + message.getReceiver()))
                ? getChatDataRedis(message.getReceiver())
                : new ArrayList<>());
        messageList.add(message);
        String json = mapper.writeValueAsString(messageList);
        redisTemplate.opsForValue().set("message-" + message.getReceiver(), json);
    }

    public List<Message> getChatDataRedis(String userID) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Message> messageList = new ArrayList<>();
        if (redisTemplate.hasKey("message-" + userID)) {
            String json = redisTemplate.opsForValue().get("message-" + userID);
            System.out.println(json);
            messageList = mapper.readValue(json, new TypeReference<List<Message>>() {
            });
        }

        return messageList;
    }

    public void removeChatDataRedis(String userID) {
        redisTemplate.delete("message-" + userID);
    }

    public boolean existRedis(String key) {
        return redisTemplate.hasKey(key);
    }

    public void setCommetRedis(String resumeUserID, String resumeCreateTime, String jobUserID, String jobCreateTime,
            String time) {
        String key = "comment-" + resumeUserID + "-" + resumeCreateTime + "-" + jobUserID + "-" + jobCreateTime;
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            long dateTime = sf.parse(time).getTime();
            long date = new Date().getTime();
            redisTemplate.opsForValue().set(key, "", (dateTime - date), TimeUnit.SECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
