package software.project.project.component.member;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends MongoRepository<MemberAccount, String> {
    MemberAccount findByUserID(String userID);
}