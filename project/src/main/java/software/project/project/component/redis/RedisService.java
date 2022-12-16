package software.project.project.component.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    public void setMemberAccountRedis(String userID, MemberAccount memberAccount){
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(memberAccount);
            redisTemplate.opsForValue().set(userID, json);
            redisTemplate.opsForValue().get(userID);
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
}
