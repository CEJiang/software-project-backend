package software.project.project.component.job;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobRepository extends MongoRepository<Job, String> {
    Job findByUserIDAndCreateTime(String userID, String createTime);
    List<Job> findByUserID(String userID);
    Job deleteByUserIDAndCreateTime(String userID, String createTime);
}