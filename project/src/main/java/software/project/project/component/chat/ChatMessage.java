package software.project.project.component.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private String sender;
    private String receiver;
    private String Message;
    private String createTime;
}
