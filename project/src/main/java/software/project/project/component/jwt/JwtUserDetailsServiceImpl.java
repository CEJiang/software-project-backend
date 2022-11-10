package software.project.project.component.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import software.project.project.component.member.MemberAccount;
import software.project.project.component.member.MemberRepository;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtMemberAccountFactory JwtMemberAccountFactory;
    @Override
    public JwtMemberAccount loadUserByUsername(String userID) throws UsernameNotFoundException {

        MemberAccount memberAccount = memberRepository.findByUserID(userID);

        if(memberAccount == null){
            throw new NotFoundException("Not found member");
        } else{
            return JwtMemberAccountFactory.create(memberAccount);
        }
    }
    
}
