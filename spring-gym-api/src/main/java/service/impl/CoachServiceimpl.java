package service.impl;

import entity.CoachEntity;
import org.springframework.stereotype.Service;
import repository.CoachRepository;
import service.CoachService;

import java.util.List;

@Service
public class CoachServiceimpl implements CoachService {

    private final CoachRepository coachRepository;
    public CoachServiceimpl(CoachRepository coachRepository) {
        this.coachRepository = coachRepository;
    }

    @Override
    public CoachEntity getCoachById(Long id) {
        return coachRepository.getById(id);
    }

    @Override
    public List<CoachEntity> getAllCoaches() {
        return coachRepository.findAll();
    }

    @Override
    public CoachEntity getCoachByHighestAmountOfClients() {
        return null;
    }

    @Override
    public CoachEntity getCoachByLowestAmountOfClients() {
        return null;
    }

    @Override
    public List<String> getWorkoutPlansByCoach(CoachEntity coachEntity) {
        return List.of();
    }

    @Override
    public void registerNewCoach(CoachEntity coachEntity) {

    }

    @Override
    public void registerNewCoaches(List<CoachEntity> coachEntities) {

    }

    @Override
    public void updateNameAndEmailById(Long id, String name, String email) {

    }

    @Override
    public void updateAmountOfClientsById(Long id) {

    }

    @Override
    public void deleteCoachById(Long id) {

    }

    @Override
    public void deleteAllCoaches() {

    }

}
