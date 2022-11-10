package software.project.project.component.jwt;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtMemberAccount implements UserDetails{
    private String id;
    private String username;
    private String userID;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public JwtMemberAccount(
        String id, 
        String username,
        String userID,
        String email,
        String password,
        Collection<? extends GrantedAuthority> authorities){
            this.id = id;
            this.username = username;
            this.userID = userID;
            this.email = email;
            this.password = password;
            this.authorities = authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
}
