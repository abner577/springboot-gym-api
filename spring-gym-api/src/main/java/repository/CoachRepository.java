package repository;

import entity.CoachEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CoachRepository extends JpaRepository<CoachEntity, Long> {

}
