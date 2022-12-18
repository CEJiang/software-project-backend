package software.project.project.component.job;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends MongoRepository<Job, String> {
    Job findByUserIDAndCreateTime(String userID, String createTime);
    List<Job> findByUserID(String userID);
    Job deleteByUserIDAndCreateTime(String userID, String createTime);
}