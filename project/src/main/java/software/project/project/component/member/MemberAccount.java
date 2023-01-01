package software.project.project.component.member;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    private List<Pair> jobCollectList = new ArrayList<>();

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    private List<Pair> resumeCollectList = new ArrayList<>();

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    private List<Pair> jobComment = new ArrayList<>();

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    private List<Pair> resumeComment = new ArrayList<>();
}