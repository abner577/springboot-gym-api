package practice.spring_gym_api.service;

import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CoachService {
    // --- GET methods ---
    CoachEntity getCoachById(Long id);
    Page<CoachEntity> getAllCoachesPageable(Pageable pageable);
    List<CoachEntity> getAllCoaches();
    List<String> getWorkoutPlansByCoachName(String name);


    // --- POST methods ---
    void registerNewCoach(CoachEntity coachEntity);
    void registerNewCoaches(List<CoachEntity> coachEntities);

    // --- PUT/PATCH methods ---
    void updateNameOrClientsById(Long id, String name, List<MemberEntity> clients);
    void updateCoachById(Long id, CoachEntity coachEntity);

    // --- DELETE methods --- *All Delete methods need to have authentication implemented, we are looking for a heading that has a ROLE-WORKER*
    void deleteCoachById(Long id);
    void deleteAllCoaches();

}
