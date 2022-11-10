package software.project.project.component.job;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends MongoRepository<Job, String> {
    Job findByUserAndOrder(String user, int order);
    List<Job> findByUser(String user);
    Job deleteByUserAndOrder(String user, int order);
}