package software.project.project.component.member;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import software.project.project.component.jwt.JwtMemberAccount;
import software.project.project.component.jwt.JwtService;
import software.project.project.component.jwt.JwtUserDetailsServiceImpl;

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
            return null;
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
        // JwtService JwtService = new JwtService();
        JwtMemberAccount userDetails = jwtUserDetailsServiceImpl.loadUserByUsername(request.getUserID());
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, 
				request.getPassword(), userDetails.getAuthorities());
		//將Authentication物件放入SecurityContext存放
		SecurityContextHolder.getContext().setAuthentication(auth); 

        String token = jwtService.generateToken(userDetails);

        return token;
    }

    public String refresh(String oldToken) {
        // final String token = oldToken.substring(tokenHead.length());
        // String username = jwtService.getUserIDFromToken(token);
        // JwtMemberAccount user = (JwtMemberAccount) jwtUserDetailsServiceImpl.loadUserByUsername(username);
        // if (jwtService.canTokenBeRefreshed(token, user.getLastPasswordResetDate())){
        //     return jwtService.refreshToken(token);
        // }
        return null;
    }
    public MemberAccount findMemberInformations(MemberAccount request) {
        return memberRepository.findByUserID(request.getUserID());
    }
}