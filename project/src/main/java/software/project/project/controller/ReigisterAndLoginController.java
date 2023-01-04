package software.project.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import software.project.project.component.chat.Message;
import software.project.project.component.exception.MemberAccountExistException;
import software.project.project.component.exception.NotFoundException;
import software.project.project.component.job.Job;
import software.project.project.component.jwt.Token;
import software.project.project.component.member.MemberAccount;
import software.project.project.component.member.MemberService;
import software.project.project.component.redis.RedisService;
import software.project.project.component.resume.Resume;

@RestController
public class ReigisterAndLoginController {
    
    @Autowired
    private MemberService MemberService;

    @Autowired
    private RedisService redisService;
    
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Validated MemberAccount request) {
        try {
            MemberService.register(request);
            return ResponseEntity.status(HttpStatus.OK).body("success");
        } catch(MemberAccountExistException e){
            // 帳號已存在
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The UserID is used");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody MemberAccount request){
        try {
            String token = MemberService.login(request);
            Token tokenObject = new Token(token);
            
            return ResponseEntity.status(HttpStatus.OK).body(tokenObject);   
        } catch(NotFoundException e){
            // 帳號不存在
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); 
        } catch (BadCredentialsException e) {
            // 密碼錯誤
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); 
        }
    }

    @GetMapping("/refresh")
    public ResponseEntity<String> refresh(@RequestBody Token oldToken){
        System.out.println("oldToken = " + oldToken.getToken());
        
        String token = MemberService.refresh(oldToken.getToken());

        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @GetMapping("/auth/getChatData/{userID}")
    public ResponseEntity<List<Message>> getChatData(@PathVariable String userID) throws JsonMappingException, JsonProcessingException{
        List<Message> messageList = redisService.getChatDataRedis(userID);
        redisService.removeChatDataRedis(userID);

        return ResponseEntity.ok(messageList);
    }

    @PostMapping("/auth/addJobCollect/{myUserID}/{userID}/{createTime}")
    public void addJobCollect(@PathVariable String myUserID, @PathVariable String userID, @PathVariable String createTime){
        MemberService.addJobCollect(myUserID, userID, createTime);
    }
    @PostMapping("/auth/addResumeCollect/{myUserID}/{userID}/{createTime}")
    public void addResumeCollect(@PathVariable String myUserID, @PathVariable String userID, @PathVariable String createTime){
        MemberService.addResumeCollect(myUserID, userID, createTime);
    }
    @PostMapping("/auth/removeJobCollect/{myUserID}/{userID}/{createTime}")
    public void removeJobCollect(@PathVariable String myUserID, @PathVariable String userID, @PathVariable String createTime){
        MemberService.removeJobCollect(myUserID, userID, createTime);
    }
    @PostMapping("/auth/removeResumeCollect/{myUserID}/{userID}/{createTime}")
    public void removeResumeCollect(@PathVariable String myUserID, @PathVariable String userID, @PathVariable String createTime){
        MemberService.removeResumeCollect(myUserID, userID, createTime);
    }

    @GetMapping("/auth/getJobCollect/{userID}")
    public ResponseEntity<List<Job>> getJobCollect(@PathVariable String userID){
        return ResponseEntity.status(HttpStatus.OK).body(MemberService.getJobCollect(userID));
    }
    @GetMapping("/auth/getResumeCollect/{userID}")
    public ResponseEntity<List<Resume>> getResumeCollect(@PathVariable String userID){
        return ResponseEntity.status(HttpStatus.OK).body(MemberService.getResumeCollect(userID));
    }
}
