package software.project.project.component.member;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberRepository extends MongoRepository<MemberAccount, String> {
    MemberAccount findByUserID(String userID);
}