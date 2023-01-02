package software.project.project.component.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private String type; // resume job
    private String userID;
    private String createTime;
    private int star;
    private String title;
    private String message;
}
