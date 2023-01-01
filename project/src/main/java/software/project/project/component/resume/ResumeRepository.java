package software.project.project.component.resume;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResumeRepository extends MongoRepository<Resume, String> {
    Resume findByUserIDAndCreateTime(String userID, String createTime);
    List<Resume> findByUserID(String userID);
    Resume deleteByUserIDAndCreateTime(String userID, String createTime);
}