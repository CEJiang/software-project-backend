package software.project.project.component.comment;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String>{
    List<Comment> findByTypeAndUserIDAndCreateTime(String type, String userID, String createTime);
    List<Comment> findByUserID(String userID);
}
