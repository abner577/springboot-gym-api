package practice.spring_gym_api.service;

import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface CoachService {
    // --- GET methods ---
    CoachEntity getCoachById(Long id);
    Page<CoachEntity> getAllCoachesPageable(Pageable pageable);
    List<CoachEntity> getAllCoaches();
    List<String> getWorkoutPlansByCoachName(String name);
    Set<MemberEntity> getAllClientsByCoachId(Long id);


    // --- POST methods ---
    void registerNewCoach(CoachEntity coachEntity);
    void registerNewCoaches(List<CoachEntity> coachEntities);

    // --- PUT/PATCH methods ---
    void updateNameById(Long id, String name);
    void updateClientsById(Long id, Set<MemberEntity> clients);
    void updateCoachByIdAndEmail(Long id,String email, CoachEntity coachEntity);

    // --- DELETE methods --- *All Delete methods need to have authentication implemented, we are looking for a heading that has a ROLE-WORKER*
    void deleteCoachById(Long id);
    void deleteAllCoaches();

}
