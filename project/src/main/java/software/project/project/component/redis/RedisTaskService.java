package software.project.project.component.redis;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class RedisTaskService extends KeyExpirationEventMessageListener {

    Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    public RedisTaskService(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }


    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();    // 將拿到的過期鍵使用之前拼接時的特殊符號分割成字符數組
        String[] expiredKeyArr = expiredKey.split("\\|");
        String businessSign = expiredKeyArr[0].toString();
        String expiredTimeSign = expiredKeyArr[1].toString();
        String othersParm = expiredKeyArr[2].toString();
        
        logger.info(businessSign + expiredTimeSign + othersParm);
        Date date = new Date();// 只有本業務才執行以下操作
        if (businessSign.equals("business1")) {
            if (expiredTimeSign.equals("key10S")) {// 定時十秒鐘後業務處理
                logger.info("十秒鐘時的時間："+ date);
                logger.info("定時任務10秒鐘已到, 下面處理相關業務邏輯代碼！！！");
                logger.info("10秒鐘後的業務邏輯代碼, 其他業務參數" + othersParm);
            } else if (expiredTimeSign.equals("key20S")) {// 定時十秒鐘後業務處理
                logger.info("二十秒鐘時的時間："+ date);
                logger.info("定時任務20秒鐘已到, 下面處理相關業務邏輯代碼！！！");
                logger.info("20秒鐘後的業務邏輯代碼, 其他業務參數" + othersParm);
            }
        } else {
            logger.error("非business1業務不做處理");
        }
    }
}
