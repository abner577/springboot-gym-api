package service.impl;

import entity.CoachEntity;
import entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import repository.CoachRepository;
import service.CoachService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
    public void updateNameOrClientsById(Long id, String name, List<MemberEntity> clients) {
        if(name == null && clients == null) throw new IllegalStateException("Provide either a new name or a new client name to update credentials");
        if(coachRepository.existsByName(name)) throw new IllegalStateException("Coach with a name of: " + name + " already exists");

        CoachEntity coachToUpdate = coachRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Coach with an id of: " + id + " doesnt exist"));

        if(name != null && name.length() > 0) coachToUpdate.setName(name);
        else throw new IllegalStateException("New name cannot be null or an empty string");

        HashSet<MemberEntity> newClientSet = coachToUpdate.getClients();
        newClientSet.addAll(clients);
        if(clients != null && clients.size() > 0) {
            coachToUpdate.setClients(newClientSet);
            coachRepository.save(coachToUpdate);
        } else throw new IllegalStateException("Client/clients cannot be empty or null");
    }

    @Override
    public void deleteCoachById(Long id) {
        if(coachRepository.existsById(id)) coachRepository.deleteById(id);
        else throw new IllegalStateException("Coach with an id of: " + id + " doesnt exist");
    }

    @Override
    public void deleteAllCoaches() {
        coachRepository.deleteAll();
    }

}
