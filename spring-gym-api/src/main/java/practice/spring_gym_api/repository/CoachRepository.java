package practice.spring_gym_api.repository;

import practice.spring_gym_api.entity.CoachEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoachRepository extends JpaRepository<CoachEntity, Long> {

    /*
    @Query("SELECT c FROM CoachEntity c ORDER BY SIZE(c.client.orEs) DESC")
    CoachEntity findCoachWithMostClients(Pageable pageable);
    */

    Optional<CoachEntity> findCoachByName(String name);
    Boolean existsByName(String name);
    boolean existsByEmail(String email);
    CoachEntity findByEmail(String email);
}
