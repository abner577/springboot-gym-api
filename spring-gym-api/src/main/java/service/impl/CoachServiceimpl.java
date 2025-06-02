package service.impl;

import entity.CoachEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import repository.CoachRepository;
import service.CoachService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CoachServiceimpl implements CoachService {

    private final CoachRepository coachRepository;
    public CoachServiceimpl(CoachRepository coachRepository) {
        this.coachRepository = coachRepository;
    }

    @Override
    public CoachEntity getCoachById(Long id) {
        return coachRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Coach with an id of: " + id + " doesnt exist"));
    }

    @Override
    public Page<CoachEntity> getAllCoachesPageable(Pageable pageable) {
        return coachRepository.findAll(pageable);
    }

    @Override
    public List<CoachEntity> getAllCoaches() {
        List<CoachEntity> list =  coachRepository.findAll();
        if(list.isEmpty()) throw new IllegalStateException("No coaches currently registered");
        return list;
    }

    @Override
    public List<String> getWorkoutPlansByCoachName(String name) {
        CoachEntity coachEntity = coachRepository.findCoachByName(name)
                .orElseThrow(() -> new IllegalStateException("Coach with a name of: " + name + " doesnt exist"));
        return coachEntity.getWorkoutPlans();
    }

    @Override
    public void registerNewCoach(CoachEntity coachEntity) {
        Optional<CoachEntity> coachEntity1 = coachRepository.findCoachByName(coachEntity.getName());
        if(coachEntity1.isPresent()) throw new IllegalStateException("Coach with a name of: " + coachEntity.getName() + " already exists");
        coachRepository.save(coachEntity);
    }

    @Override
    public void registerNewCoaches(List<CoachEntity> coachEntities) {
        for(CoachEntity coachEntity : coachEntities){
            if(coachRepository.existsByName(coachEntity.getName())) throw new IllegalStateException("Coach with a name of: " + coachEntity.getName() + " already exists");
        }
        coachRepository.saveAll(coachEntities);
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
