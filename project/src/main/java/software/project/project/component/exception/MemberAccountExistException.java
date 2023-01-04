package software.project.project.component.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class MemberAccountExistException extends RuntimeException {

    public MemberAccountExistException(){}
    public MemberAccountExistException(String message) {
        super(message);
    }

}
