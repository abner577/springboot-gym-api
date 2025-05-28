package service;

import entity.CoachEntity;
import java.util.List;

public interface CoachService {
    // --- GET methods ---
    CoachEntity getCoachById(Long id);
    List<CoachEntity> getAllCoaches();
    CoachEntity getCoachByHighestAmountOfClients();
    CoachEntity getCoachByLowestAmountOfClients();
    List<String> getWorkoutPlansByCoach(CoachEntity coachEntity);


    // --- POST methods ---
    void registerNewCoach(CoachEntity coachEntity);
    void registerNewCoaches(List<CoachEntity> coachEntities);

    // --- PUT/PATCH methods ---
    void updateNameAndEmailById(Long id, String name, String email);
    void updateAmountOfClientsById(Long id); // --> figure out how to implement this later

    // --- DELETE methods --- *All Delete methods need to have authentication implemented, we are lookign for a heading that has a ROLE-WORKER*
    void deleteCoachById(Long id);
    void deleteAllCoaches();

}
