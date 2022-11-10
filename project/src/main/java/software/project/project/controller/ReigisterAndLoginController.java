package software.project.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import software.project.project.component.member.MemberAccount;
import software.project.project.component.member.MemberService;

@RestController
public class ReigisterAndLoginController {
    
    @Autowired
    private MemberService MemberService;

    @PostMapping("/register")
    public void register(@RequestBody @Validated MemberAccount request) {
        // ResponseEntity<MemberAccount>
        MemberService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody MemberAccount request){
        String token = MemberService.login(request);
        return ResponseEntity.ok(token);   
    }
}
