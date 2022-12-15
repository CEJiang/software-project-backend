package software.project.project.component.mail;

import java.util.Properties;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import software.project.project.component.redis.RedisService;

@Service
public class MailService {
    @Autowired
    private MailConfig mailConfig;

    @Autowired
    private RedisService redisService;

    private JavaMailSenderImpl mailSender;

    private final int VERIFYCODE_LENGTH = 6;

    @PostConstruct
    private void init() {
        mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailConfig.getHost());
        mailSender.setPort(mailConfig.getPort());
        mailSender.setUsername(mailConfig.getUsername());
        mailSender.setPassword(mailConfig.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", mailConfig.getAuthEnabled());
        props.put("mail.smtp.starttls.enable", mailConfig.getStarttlsEnabled());
        props.put("mail.transport.protocol", mailConfig.getProtocol());
    }

    public Mail sendMail(Mail request) {
        String verifyCode = generateVerifyCode();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailConfig.getUsername());
        message.setTo(request.getReceivers());
        message.setSubject("馬上上工 傳送驗證碼(禁止回信)");
        message.setText("您的驗證碼是 " + verifyCode);

        try {
            mailSender.send(message);
            redisService.setEmailRedis(request.getReceivers(), verifyCode);
        } catch (MailAuthenticationException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return request;
    }

    public String generateVerifyCode(){
        String verifyCodeTotalString  = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
		String verifyCode = "";
		Random rand = new Random();
		for(int i = 0; i < VERIFYCODE_LENGTH; ++i){
			int index = rand.nextInt(verifyCodeTotalString.length());
			verifyCode += verifyCodeTotalString.charAt(index);
		}
		System.out.println(verifyCode);

        return verifyCode;
    }

    public boolean mailCheck(String verifyCode, String email){
        String redisVerifyCode = redisService.getEmailRedis(email);
        if(verifyCode.equals(redisVerifyCode)){
            redisService.deleteEmailRedis(email);
            return true;
        } 
        else return false;
    }
}