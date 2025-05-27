package repository;

import entity.CoachEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoachRepository extends JpaRepository<CoachEntity, Long> {
    Optional<CoachEntity> findByName(String name);
}
