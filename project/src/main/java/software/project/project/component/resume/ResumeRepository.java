package software.project.project.component.resume;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepository extends MongoRepository<Resume, String> {
    Resume findByUserAndOrder(String user, int order);
    List<Resume> findByUser(String user);
    Resume deleteByUserAndOrder(String user, int order);
}