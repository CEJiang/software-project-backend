package software.project.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import software.project.project.component.exception.NotFoundException;
import software.project.project.component.job.Job;
import software.project.project.component.jwt.Token;
import software.project.project.component.member.MemberAccount;
import software.project.project.component.member.MemberService;
import software.project.project.component.resume.Resume;

@RestController
public class ReigisterAndLoginController {
    
    @Autowired
    private MemberService MemberService;
    
    @PostMapping("/register")
    public void register(@RequestBody @Validated MemberAccount request) {
        try{
            MemberService.register(request);
        } catch(NotFoundException e){
            throw new NotFoundException();
        }
        
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody MemberAccount request){
        try {
            String token = MemberService.login(request);
            
            Token tokenObject = new Token(token);
            
            return ResponseEntity.ok().body(tokenObject);   
        } catch(NotFoundException e){
            throw new NotFoundException();
        } catch (Exception e) {
            // return ResponseEntity.badRequest().body("fail");
            return null;
        }
    }

    @GetMapping("/refresh")
    public ResponseEntity<String> refresh(@RequestBody Token oldToken){
        System.out.println("oldToken = " + oldToken.getToken());
        
        String token = MemberService.refresh(oldToken.getToken());

        return ResponseEntity.ok().body(token);
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
    public List<Job> getJobCollect(@PathVariable String userID){
        return MemberService.getJobCollect(userID);
    }
    @GetMapping("/auth/getResumeCollect/{userID}")
    public List<Resume> getResumeCollect(@PathVariable String userID){
        return MemberService.getResumeCollect(userID);
    }
}
