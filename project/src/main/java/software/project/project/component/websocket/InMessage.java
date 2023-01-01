package software.project.project.component.websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InMessage {
    private String sender;
    private String receiver;
    private String message;

    @Override
    public String toString() {
        return "sender = " + sender + "\nreceiver = " + receiver + "\nmessage = " + message;
    }
}
