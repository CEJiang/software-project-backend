package software.project.project.component.jwt;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import software.project.project.component.member.MemberAccount;

import org.springframework.security.core.GrantedAuthority;

@Component
public class JwtMemberAccountFactory {
    public JwtMemberAccount create(MemberAccount user) {
        return new JwtMemberAccount(
                user.getId(),
                user.getUsername(),
                user.getUserID(),
                user.getEmail(),
                user.getPassword(),
                mapToGrantedAuthorities(user.getRoles())
        );
    }

    private List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
