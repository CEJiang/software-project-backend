package software.project.project.component.member;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import software.project.project.component.Pair;

@Getter
@Setter
@Document("memberAccount")
public class MemberAccount {
    @Id
    private String id;

    @NotNull
    private String username;

    @Indexed(unique = true)
    @NotNull
    private String userID;

    @Email(message = "信箱格式錯誤")
    @NotNull
    private String email;

    @NotNull
    private String password;

    private List<String> roles;

    private List<Pair> JobColletList;
    private List<Pair> ResumeColletList;

    @Override
    public String toString(){
        return email;
    }
}



