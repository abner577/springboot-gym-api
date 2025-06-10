package practice.spring_gym_api.repository;

import practice.spring_gym_api.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Member;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    boolean findMemberByName(String name);
    boolean existsByEmail(String email);
    MemberEntity findMemberByEmail(String email);

}
