package practice.spring_gym_api.service.impl;

import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import practice.spring_gym_api.repository.CoachRepository;
import practice.spring_gym_api.service.CoachService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        Optional<CoachEntity> coachEntity1 = Optional.ofNullable(coachRepository.findByEmail(coachEntity.getEmail()));
        if(coachEntity1.isPresent()) throw new IllegalStateException("Coach with an email of: " + coachEntity.getEmail() + " already exists");
        coachRepository.save(coachEntity);
    }

    @Override
    public void registerNewCoaches(List<CoachEntity> coachEntities) {
        for(CoachEntity coachEntity : coachEntities){
            if(coachRepository.existsByEmail(coachEntity.getEmail())) throw new IllegalStateException("Coach with an email of: " + coachEntity.getEmail() + " already exists");
        }
        coachRepository.saveAll(coachEntities);
    }

    @Override
    public void updateNameOrClientsById(Long id, String name, List<MemberEntity> clients) {
        if(name == null || clients == null) throw new IllegalStateException("Provide either a new name or a new client name to update credentials");

        CoachEntity coachToUpdate = coachRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Coach with an id of: " + id + " doesnt exist"));

        if(name != null && name.length() > 0) coachToUpdate.setName(name);
        else throw new IllegalStateException("New name cannot be null or an empty string");

        Set<MemberEntity> newClientSet = coachToUpdate.getClients();
        newClientSet.addAll(clients);
        if(clients != null && clients.size() > 0) {
            coachToUpdate.setClients(newClientSet);
            coachRepository.save(coachToUpdate);
        } else throw new IllegalStateException("Client/clients cannot be empty or null");
    }

    @Override
    public void updateCoachByIdAndEmail(Long id, String email, CoachEntity coachEntity) {
        CoachEntity coachEntityToUpdate = coachRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Coach with an id of: " + id + " doesnt exist"));

        if(!coachRepository.existsByEmail(email)){
            throw new IllegalStateException("Coach with an email of: " + email + " doesnt exist");
        }
        else if (coachEntityToUpdate != coachRepository.findByEmail(email)) {
            throw new IllegalStateException("Coach with an id of: " + id + " is not the same coach that has an email of: " + email);
        }

        coachEntityToUpdate.setName(coachEntity.getName());
        coachEntityToUpdate.setClients(coachEntity.getClients());
        coachEntityToUpdate.setAge(coachEntity.getAge());
        coachEntityToUpdate.setWorkoutPlans(coachEntity.getWorkoutPlans());
        coachEntityToUpdate.setDateOfBirth(coachEntity.getDateOfBirth());
        coachEntityToUpdate.setEmail(coachEntity.getEmail());
        coachRepository.save(coachEntity);
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
