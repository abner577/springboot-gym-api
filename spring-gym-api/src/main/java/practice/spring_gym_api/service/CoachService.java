package practice.spring_gym_api.service;

import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import practice.spring_gym_api.entity.WorkerEntity;

import java.util.List;
import java.util.Set;

public interface CoachService {
    // --- GET methods ---
    CoachEntity getCoachById(Long id);
    Page<CoachEntity> getAllCoachesPageable(Pageable pageable);
    List<CoachEntity> getAllCoaches();
    CoachEntity getCoachWithHighestClients();
    CoachEntity getCoachWithLowestClients();
    List<String> getWorkoutPlansByCoachName(String name);
    Set<MemberEntity> getAllClientsByCoachId(Long id);
    List<CoachEntity> getAllCoachesThatAreAvaliable();
    CoachEntity getCoachByCoachCode(Long id, String coachCode);


    // --- POST methods ---
    void registerNewCoach(CoachEntity coachEntity);
    void registerNewCoaches(List<CoachEntity> coachEntities);

    // --- PUT/PATCH methods ---
    void updateNameByIdAndEmail(Long id, String name, String email);
    void addClientsByIdAndEmail(Long id, String email, Set<MemberEntity> clients);
    void replaceClientListByIdAndEmail(Long id, String email, List<Long> listOfIds);
    void updateCoachByIdAndEmail(Long id, String email, CoachEntity coachEntity);
    void updateWorkoutPlans(Long id, String email, List<String> workoutPlans);
    void updateRoleOfACoach(Long id, String email, String role);
    void updateCodeOfACoach(Long id, String email, String coachCode);

    // --- DELETE methods --- *All Delete methods need to have authentication implemented, we are looking for a heading that has a ROLE-WORKER*
    void deleteCoachById(Long id);
    void deleteAllCoaches();

}
