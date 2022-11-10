package software.project.project.component.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void setRedis(String email, String verifyCode){
        redisTemplate.opsForValue().set(email, verifyCode, 5, TimeUnit.MINUTES);
    }

    public String getRedis(String email){
        return redisTemplate.opsForValue().get(email);
    }
    
    public void deleteRedis(String email){
        redisTemplate.delete(email);
    }
}
