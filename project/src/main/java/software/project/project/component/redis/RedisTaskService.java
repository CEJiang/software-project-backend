package software.project.project.component.redis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import software.project.project.component.member.MemberAccount;
import software.project.project.component.member.MemberRepository;
import software.project.project.component.member.Pair;

@Service
@Component
public class RedisTaskService extends KeyExpirationEventMessageListener {

    Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RedisService redisService;

    public RedisTaskService(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        try {
            String[] expiredKeyArr = expiredKey.split("-");
            String businessSign = expiredKeyArr[0].toString();
            String resumeUserIDSign = expiredKeyArr[1].toString();
            String resumeCreateTimeParm = expiredKeyArr[2].toString();
            String jobUserIDSign = expiredKeyArr[3].toString();
            String jobCreateTimeParm = expiredKeyArr[4].toString();
            
            if (businessSign.equals("comment")) {
                // add jobComment
                MemberAccount resumeMemberAccount = redisService.getMemberAccountRedis(resumeUserIDSign);
                resumeMemberAccount.getJobComment().add(new Pair(jobUserIDSign, jobCreateTimeParm));

                redisService.setMemberAccountRedis(resumeMemberAccount);
                memberRepository.save(resumeMemberAccount);

                MemberAccount jobMemberAccount = redisService.getMemberAccountRedis(jobUserIDSign);
                jobMemberAccount.getResumeComment().add(new Pair(resumeUserIDSign, resumeCreateTimeParm));
                
                redisService.setMemberAccountRedis(jobMemberAccount);
                memberRepository.save(jobMemberAccount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
