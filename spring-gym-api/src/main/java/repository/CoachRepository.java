package repository;

import entity.CoachEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoachRepository extends JpaRepository<CoachEntity, Long> {
}
