package practice.spring_gym_api.repository;

import practice.spring_gym_api.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    boolean findMemberByName(String name);

}
