package software.project.project.component.comment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public void save(Comment comment) {
        commentRepository.save(comment);    
    }
    
    public List<Comment> getUserComment(String userID){
        List<Comment> commentList = commentRepository.findByUserID(userID);
        return commentList;
    }

    public List<Comment> getTypeComment(String type, String userID, String createTime){
        List<Comment> commentList = commentRepository.findByTypeAndUserIDAndCreateTime(type, userID, createTime);

        return commentList;
    }
}
