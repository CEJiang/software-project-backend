package software.project.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import software.project.project.component.exception.NotFoundException;
import software.project.project.component.jwt.Token;
import software.project.project.component.member.MemberAccount;
import software.project.project.component.member.MemberService;

@RestController
public class ReigisterAndLoginController {
    
    @Autowired
    private MemberService MemberService;

    @PostMapping("/register")
    public void register(@RequestBody MemberAccount request) {
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
}
