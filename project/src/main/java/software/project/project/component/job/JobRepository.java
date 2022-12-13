package software.project.project.component.job;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends MongoRepository<Job, String> {
    Job findByUserAndCreateTime(String user, String createTime);
    List<Job> findByUser(String user);
    Job deleteByUserAndCreateTime(String user, String createTime);
}