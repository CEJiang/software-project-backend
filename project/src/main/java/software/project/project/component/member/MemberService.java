package software.project.project.component.member;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import software.project.project.component.exception.NotFoundException;
import software.project.project.component.job.Job;
import software.project.project.component.job.JobRepository;
import software.project.project.component.jwt.JwtMemberAccount;
import software.project.project.component.jwt.JwtService;
import software.project.project.component.jwt.JwtUserDetailsServiceImpl;
import software.project.project.component.redis.RedisService;
import software.project.project.component.resume.Resume;
import software.project.project.component.resume.ResumeRepository;

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
    private RedisService redisService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JobRepository jobRepository;

    @Autowired 
    private ResumeRepository resumeRepository;

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
        
        if(findMemberInformations(request.getUserID()) != null){
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
        
        System.out.println("getUserID = " + request.getUserID());
        MemberAccount memberAccount = findMemberInformations(request.getUserID());
        System.out.println(memberAccount.toString());
        redisService.setMemberAccountRedis(request.getUserID(), memberAccount);
        
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

    public MemberAccount findMemberInformations(String userID) {
        return memberRepository.findByUserID(userID);
    }

    public void addJobCollect(String myUserID, String userID, String createTime){
        MemberAccount member = redisService.getMemberAccountRedis(myUserID);
        member.getJobColletList().add(new Pair(userID, createTime));

        redisService.setMemberAccountRedis(myUserID, member);
        memberRepository.save(member);
    }

    public void addResumeCollect(String myUserID, String userID, String createTime){
        MemberAccount member = redisService.getMemberAccountRedis(myUserID);
        member.getResumeColletList().add(new Pair(userID, createTime));

        redisService.setMemberAccountRedis(myUserID, member);
        memberRepository.save(member);
    }

    public void removeJobCollect(String myUserID, String userID, String createTime){
        MemberAccount member = redisService.getMemberAccountRedis(myUserID);
        member.getJobColletList().removeIf((Pair a) -> a.getKey().equals(userID) && a.getValue().equals(createTime));

        redisService.setMemberAccountRedis(myUserID, member);
        memberRepository.save(member);
    }

    public void removeResumeCollect(String myUserID, String userID, String createTime){
        MemberAccount member = redisService.getMemberAccountRedis(myUserID);
        member.getResumeColletList().removeIf((Pair a) -> a.getKey().equals(userID) && a.getValue().equals(createTime));
        
        redisService.setMemberAccountRedis(myUserID, member);
        memberRepository.save(member);
    }

    public List<Job> getJobCollect(String userID) {
        MemberAccount member = redisService.getMemberAccountRedis(userID);
        List<Pair> jobIDCollect = member.getJobColletList();
        List<Job> jobCollect = new ArrayList<>();
        for(Pair job : jobIDCollect){
            jobCollect.add(jobRepository.findByUserAndCreateTime(job.getKey(), job.getValue()));
        }
        return jobCollect;
    }

    public List<Resume> getResumeCollect(String userID) {
        MemberAccount member = redisService.getMemberAccountRedis(userID);
        List<Pair> resumeIDCollect = member.getResumeColletList();
        List<Resume> resumeCollect = new ArrayList<>();
        for(Pair resume : resumeIDCollect){
            resumeCollect.add(resumeRepository.findByUserAndCreateTime(resume.getKey(), resume.getValue()));
        }
        return resumeCollect;
    }
}