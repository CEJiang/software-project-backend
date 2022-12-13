package software.project.project.component.resume;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepository extends MongoRepository<Resume, String> {
    Resume findByUserAndCreateTime(String user, String createTime);
    List<Resume> findByUser(String user);
    Resume deleteByUserAndCreateTime(String user, String createTime);
}