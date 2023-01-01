package software.project.project.component.chat;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document("Message")
public class Message {
    private String sender;
    private String receiver;
    private String createTime;
    private String message;
    private String type; // message resume interview
    
    @Override
    public String toString() {
        return "sender = " + sender + "\nreceiver = " + receiver + "\nmessage = " + message + "\ncreateTime = " + createTime + "\ntype = " + type;
    }
}
