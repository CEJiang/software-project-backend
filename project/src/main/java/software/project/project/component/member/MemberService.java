package software.project.project.component.member;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import software.project.project.component.exception.NotFoundException;
import software.project.project.component.jwt.JwtMemberAccount;
import software.project.project.component.jwt.JwtService;
import software.project.project.component.jwt.JwtUserDetailsServiceImpl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class MemberService {
    
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    private MemberRepository memberRepository;
    private JwtUserDetailsServiceImpl jwtUserDetailsServiceImpl;
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public MemberService(
            JwtService jwtService,
            JwtUserDetailsServiceImpl jwtUserDetailsServiceImpl,
            MemberRepository memberRepository) {
        this.jwtService = jwtService;
        this.jwtUserDetailsServiceImpl = jwtUserDetailsServiceImpl;
        this.memberRepository = memberRepository;
    }

    public MemberAccount register(MemberAccount request) {
        
        if(findMemberInformations(request) != null){
            throw new NotFoundException("");
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        MemberAccount MemberInformations = new MemberAccount();
        
        MemberInformations.setUserID(request.getUserID());
        MemberInformations.setUsername(request.getUsername());
        MemberInformations.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        MemberInformations.setEmail(request.getEmail());
        MemberInformations.setRoles(Arrays.asList("ROLE_USER"));

        return memberRepository.insert(MemberInformations);
    }

    public String login(MemberAccount request){
        JwtMemberAccount userDetails = jwtUserDetailsServiceImpl.loadUserByUsername(request.getUserID());
        Authentication authToken = new UsernamePasswordAuthenticationToken(request.getUserID(), request.getPassword());

        Authentication auth = authenticationManager.authenticate(authToken);

        SecurityContextHolder.getContext().setAuthentication(auth); 

        String token = jwtService.generateToken(userDetails);

        return token;
    }

    public String refresh(String oldToken) {
        // String tokenHeader = oldToken.substring(tokenHead.length());
        String username = jwtService.getUserIDFromToken(oldToken);
        JwtMemberAccount user = (JwtMemberAccount) jwtUserDetailsServiceImpl.loadUserByUsername(username);
        String token = jwtService.refreshToken(user);
        return token;
    }
    public MemberAccount findMemberInformations(MemberAccount request) {
        return memberRepository.findByUserID(request.getUserID());
    }
}