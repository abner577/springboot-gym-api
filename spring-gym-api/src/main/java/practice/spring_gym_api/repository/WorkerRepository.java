package practice.spring_gym_api.repository;

import practice.spring_gym_api.entity.WorkerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerRepository extends JpaRepository<WorkerEntity, Long>{

    boolean existsByEmail(String email);
}
