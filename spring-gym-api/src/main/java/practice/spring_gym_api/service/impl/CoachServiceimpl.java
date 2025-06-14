package practice.spring_gym_api.service.impl;

import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import practice.spring_gym_api.repository.CoachRepository;
import practice.spring_gym_api.repository.MemberRepository;
import practice.spring_gym_api.service.CoachService;

import java.util.*;

/**
 * Service implementation for Coach-related operations.
 * Handles business logic and interaction with the CoachRepository.
 */
@Service
public class CoachServiceimpl implements CoachService {

    private final CoachRepository coachRepository;
    private final MemberRepository memberRepository;
    public CoachServiceimpl(CoachRepository coachRepository, MemberRepository memberRepository) {
        this.coachRepository = coachRepository;
        this.memberRepository = memberRepository;
    }

    /**
     * Retrieves a coach by their ID.
     *
     * @param id The coach's unique identifier
     * @return CoachEntity associated with the ID
     * @throws IllegalStateException if the coach is not found
     */
    @Override
    public CoachEntity getCoachById(Long id) {
        return coachRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Coach with an id of: " + id + " doesnt exist"));
    }

    /**
     * Retrieves all coaches in a pageable format.
     *
     * @param pageable Pageable object with pagination info
     * @return Page of CoachEntity objects
     */
    @Override
    public Page<CoachEntity> getAllCoachesPageable(Pageable pageable) {
        return coachRepository.findAll(pageable);
    }

    /**
     * Retrieves all registered coaches as a list.
     *
     * @return List of CoachEntity
     * @throws IllegalStateException if no coaches are found
     */
    @Override
    public List<CoachEntity> getAllCoaches() {
        List<CoachEntity> list =  coachRepository.findAll();
        if(list.isEmpty()) throw new IllegalStateException("No coaches currently registered");
        return list;
    }

    /**
     * Retrieves workout plans for a specific coach by name.
     *
     * @param name Name of the coach
     * @return List of workout plan names
     * @throws IllegalStateException if the coach does not exist
     */
    @Override
    public List<String> getWorkoutPlansByCoachName(String name) {
        CoachEntity coachEntity = coachRepository.findCoachByName(name)
                .orElseThrow(() -> new IllegalStateException("Coach with a name of: " + name + " doesnt exist"));
        return coachEntity.getWorkoutPlans();
    }

    /**
     * Returns all clients (members) assigned to a coach by their ID.
     *
     * @param id Coach ID
     * @return Set of MemberEntity clients; empty set if none exist
     * @throws IllegalStateException if coach is not found
     */
    @Override
    public Set<MemberEntity> getAllClientsByCoachId(Long id) {
       CoachEntity coachEntityToUpdate = coachRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Coach with an id of: " + id + " doesnt exist"));

       if(coachEntityToUpdate.getClients().size() > 0) return coachEntityToUpdate.getClients();
       else return new HashSet<>();
    }

    @Override
    public List<CoachEntity> getAllCoachesThatAreAvaliable() {
        List<CoachEntity> coachEntities = coachRepository.findAll();
        List<CoachEntity> coachEntitiesToReturn = new ArrayList<>();

        for(CoachEntity coachEntity : coachEntities){
            if(coachEntity.getClients().size() == 0) coachEntitiesToReturn.add(coachEntity);
        }
        return coachEntitiesToReturn;
    }

    /**
     * Registers a new coach, ensuring email uniqueness.
     *
     * @param coachEntity New coach to be registered
     * @throws IllegalStateException if a coach with the given email already exists
     */
    @Override
    public void registerNewCoach(CoachEntity coachEntity) {
        Optional<CoachEntity> coachEntity1 = Optional.ofNullable(coachRepository.findByEmail(coachEntity.getEmail()));
        if(coachEntity1.isPresent()) throw new IllegalStateException("Coach with an email of: " + coachEntity.getEmail() + " already exists");
        coachRepository.save(coachEntity);
    }

    /**
     * Registers multiple coaches after checking for email duplicates.
     *
     * @param coachEntities List of coaches to be saved
     * @throws IllegalStateException if any email already exists
     */
    @Override
    public void registerNewCoaches(List<CoachEntity> coachEntities) {
        for(CoachEntity coachEntity : coachEntities){
            if(coachRepository.existsByEmail(coachEntity.getEmail())) throw new IllegalStateException("Coach with an email of: " + coachEntity.getEmail() + " already exists");
        }
        coachRepository.saveAll(coachEntities);
    }

    /**
     * Updates a coach's name by their ID.
     *
     * @param id   Coach ID
     * @param name New name to assign
     * @throws IllegalStateException if ID is invalid or name is null/empty
     */
    @Override
    public void updateNameByIdAndEmail(Long id, String name, String email) {
        if(name == null || name.isEmpty()) throw new IllegalStateException("Name cannot be null or an empty string");

        CoachEntity coachEntityToUpdateNameFromId = coachRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Coach with an id of: " + id + " doesnt exist"));

        CoachEntity coachEntityToUpdateNameFromEmail = coachRepository.findByEmail(email);
        if(coachEntityToUpdateNameFromEmail == null) throw new IllegalStateException("Coach with an email of: " + email + " doesnt exist");
        if(!coachEntityToUpdateNameFromId.equals(coachEntityToUpdateNameFromEmail)) throw new IllegalStateException("Coach with an email of: " + email + " isnt the same coach with an id of: " + id);

        coachEntityToUpdateNameFromId.setName(name);
        coachRepository.save(coachEntityToUpdateNameFromId);
    }

    /**
     * Adds clients to a coach. Sets the back-reference in each MemberEntity.
     * Does not override existing clients — appends to the list.
     *
     * @param id             Coach ID
     * @param memberEntities Set of new clients to associate with the coach
     * @throws IllegalStateException if coach is not found
     */
    @Override
    public void updateClientsByIdAndEmail(Long id, String email, Set<MemberEntity> memberEntities) {
        CoachEntity coachEntityToUpdateClientsID = coachRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Coach with an id of: " + id + " doesnt exist"));

        CoachEntity coachEntityToUpdateClientsEmail = coachRepository.findByEmail(email);
        if(coachEntityToUpdateClientsEmail == null) throw new IllegalStateException("Coach with an email of: " + email + " doesnt exist");
        if(!coachEntityToUpdateClientsID.equals(coachEntityToUpdateClientsEmail)) throw new IllegalStateException("Coach with an email of: " + email + " isnt the same coach with an id of: " + id);

        for(MemberEntity memberEntity : memberEntities){
            System.out.println("Assigning coach to: " + memberEntity.getEmail());
            memberEntity.setCoachedBy(coachEntityToUpdateClientsID);
            }

        coachEntityToUpdateClientsID.getClients().addAll(memberEntities);
        coachRepository.save(coachEntityToUpdateClientsID);
    }

    /**
     * Fully updates a coach by ID and email, ensuring they match the record being updated.
     *
     * @param id           Coach ID
     * @param email        Coach email for identity verification
     * @param coachEntity  Updated coach entity
     * @throws IllegalStateException if ID/email do not match or do not exist
     */
    @Override
    public void updateCoachByIdAndEmail(Long id, String email, CoachEntity coachEntity) {
        CoachEntity coachEntityToUpdate = coachRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Coach with an id of: " + id + " doesnt exist"));

        CoachEntity coachEntityToUpdateEmail = coachRepository.findByEmail(email);
        if(coachEntityToUpdateEmail == null) throw new IllegalStateException("Coach with an email of: " + email + " doesnt exist");
        if(!coachEntityToUpdate.equals(coachEntityToUpdateEmail)) throw new IllegalStateException("Coach with an email of: " + email + " isnt the same coach with an id of: " + id);

        coachEntityToUpdate.setName(coachEntity.getName());
        coachEntityToUpdate.setClients(coachEntity.getClients());
        coachEntityToUpdate.setAge(coachEntity.getAge());
        coachEntityToUpdate.setClients(coachEntity.getClients());
        coachEntityToUpdate.setWorkoutPlans(coachEntity.getWorkoutPlans());
        coachEntityToUpdate.setDateOfBirth(coachEntity.getDateOfBirth());
        coachEntityToUpdate.setEmail(coachEntity.getEmail());
        coachRepository.save(coachEntity);
    }

    /**
     * Deletes a coach by their ID.
     *
     * @param id Coach ID
     * @throws IllegalStateException if no coach exists with the given ID
     */
    @Override
    public void deleteCoachById(Long id) {
        if(coachRepository.existsById(id)) coachRepository.deleteById(id);
        else throw new IllegalStateException("Coach with an id of: " + id + " doesnt exist");
    }

    /**
     * Deletes all coaches from the repository.
     */
    @Override
    public void deleteAllCoaches() {coachRepository.deleteAll();}
}
