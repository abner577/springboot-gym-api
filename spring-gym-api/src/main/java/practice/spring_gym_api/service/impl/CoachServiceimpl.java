package practice.spring_gym_api.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import practice.spring_gym_api.dto.CoachMapper;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import practice.spring_gym_api.entity.WorkerEntity;
import practice.spring_gym_api.repository.CoachRepository;
import practice.spring_gym_api.repository.MemberRepository;
import practice.spring_gym_api.repository.WorkerRepository;
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
    private final WorkerRepository workerRepository;
    private final CoachMapper coachMapper;

    public CoachServiceimpl(CoachRepository coachRepository, MemberRepository memberRepository, WorkerRepository workerRepository, @Qualifier("coachMapperimpl") CoachMapper coachMapper) {
        this.coachRepository = coachRepository;
        this.memberRepository = memberRepository;
        this.workerRepository = workerRepository;
        this.coachMapper = coachMapper;
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
                .orElseThrow(() -> new NoSuchElementException("Coach with an id of: " + id + " doesnt exist"));
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
        if(list.isEmpty()) throw new NoSuchElementException("No coaches currently registered");
        return list;
    }

    @Override
    public CoachEntity getCoachWithHighestClients(){
        List<CoachEntity> list = coachRepository.findAll();
        if(list.isEmpty()) throw new NoSuchElementException("No coaches currently registered");

        int max = list.get(0).getClients().size();
        CoachEntity coachEntityToReturn = list.get(0);

        for(CoachEntity coachEntity : list){
            if(coachEntity.getClients().size() > max){
                max = coachEntity.getClients().size();
                coachEntityToReturn = coachEntity;
            }
        }
        return coachEntityToReturn;
    }

    @Override
    public CoachEntity getCoachWithLowestClients() {
       List<CoachEntity> coachEntityList = coachRepository.findAll();
        if(coachEntityList.isEmpty()) throw new NoSuchElementException("No coaches currently registered");

       int min = coachEntityList.get(0).getClients().size();
       CoachEntity entityToReturn = coachEntityList.get(0);

       for(CoachEntity coach : coachEntityList){
           if(coach.getClients().size() < min) {
               min = coach.getClients().size();
               entityToReturn = coach;
           }
       }
       return entityToReturn;
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
                .orElseThrow(() -> new NoSuchElementException("Coach with a name of: " + name + " doesn't exist"));
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
                .orElseThrow(() -> new NoSuchElementException("Coach with an id of: " + id + " doesnt exist"));

       if(coachEntityToUpdate.getClients().size() > 0) return coachEntityToUpdate.getClients();
       else throw new NoSuchElementException("Coach does not have any clients to access");
    }

    /**
     * Retrieves all coaches with no assigned clients.
     *
     * @return List of available coaches
     */
    @Override
    public List<CoachEntity> getAllCoachesThatAreAvaliable() {
        List<CoachEntity> coachEntities = coachRepository.findAll();
        if(coachEntities.isEmpty()) throw new NoSuchElementException("No coaches currently registered");

        List<CoachEntity> coachEntitiesToReturn = new ArrayList<>();

        for(CoachEntity coachEntity : coachEntities){
            if(coachEntity.getClients().size() == 0) coachEntitiesToReturn.add(coachEntity);
        }
        return coachEntitiesToReturn;
    }

    @Override
    public CoachEntity getCoachByCoachCode(Long id, String coachCode) {
        CoachEntity coachEntity = coachRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Coach with an id of: " + id + " doesnt exist"));

        CoachEntity coachByCode = coachRepository.findByCoachCode(coachCode);
        if(coachByCode == null) throw new NoSuchElementException("Coach with a code of: " + coachCode + " doesnt exist");
        if(!coachEntity.equals(coachByCode)) {
            throw new IllegalStateException("Coach with an id of: " + id + " isnt the same coach with a coach code of: " + coachCode);
        }
        return coachEntity;
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
        if(coachEntity1.isPresent()) throw new IllegalArgumentException("Coach with an email of: " + coachEntity.getEmail() + " already exists");
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
            if(coachRepository.existsByEmail(coachEntity.getEmail())) throw new IllegalArgumentException("Coach with an email of: " + coachEntity.getEmail() + " already exists");
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
        if(name == null || name.isEmpty()) throw new IllegalArgumentException("Name cannot be null or an empty string");
        if(email == null || email.isEmpty()) throw new IllegalArgumentException("Email cannot be null or an empty string");

        CoachEntity coachEntityToUpdateNameFromId = coachRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Coach with an id of: " + id + " doesnt exist"));

        CoachEntity coachEntityToUpdateNameFromEmail = coachRepository.findByEmail(email);
        if(coachEntityToUpdateNameFromEmail == null) throw new NoSuchElementException("Coach with an email of: " + email + " doesnt exist");
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
    public void addClientsByIdAndEmail(Long id, String email, Set<MemberEntity> memberEntities) {
        if(email == null || email.isEmpty()) throw new IllegalArgumentException("Email cannot be null or an empty string");

        CoachEntity coachEntityToUpdateClientsID = coachRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Coach with an id of: " + id + " doesnt exist"));

        CoachEntity coachEntityToUpdateClientsEmail = coachRepository.findByEmail(email);
        if(coachEntityToUpdateClientsEmail == null) throw new NoSuchElementException("Coach with an email of: " + email + " doesnt exist");
        if(!coachEntityToUpdateClientsID.equals(coachEntityToUpdateClientsEmail)) throw new IllegalStateException("Coach with an email of: " + email + " isnt the same coach with an id of: " + id);

        for(MemberEntity memberEntity : memberEntities){
            memberEntity.setCoachedBy(coachEntityToUpdateClientsID);
            }

        coachEntityToUpdateClientsID.getClients().addAll(memberEntities);
        coachRepository.save(coachEntityToUpdateClientsID);
    }

    /**
     * Replaces the entire client list of a coach.
     * Clears old clients’ coach references and sets new ones.
     *
     * @param id             Coach ID
     * @param email          Coach email
     * @param newClientList  New set of clients to assign to the coach
     * @throws IllegalStateException if the coach is not found or ID and email don't match
     */
    @Override
    public void replaceClientListByIdAndEmail(Long id, String email, Set<MemberEntity> newClientList) {
        CoachEntity coachEntityById = coachRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Coach with an id of: " + id + " doesnt exist"));
        CoachEntity coachEntityByEmail = coachRepository.findByEmail(email);
        if(coachEntityByEmail == null) throw new NoSuchElementException("Coach with an email of: " + email + " doesnt exist");
        if(!coachEntityById.equals(coachEntityByEmail)) throw new IllegalStateException("Coach with an email of: " + email + " isnt the same coach with an id of: " + id);

        for(MemberEntity memberEntity : coachEntityById.getClients()) memberEntity.setCoachedBy(null);

        for(MemberEntity memberEntity : newClientList) {
            memberEntity.setCoachedBy(coachEntityById);
        }
        memberRepository.saveAll(newClientList);
    }

    /**
     * Updates a coach's workout plans.
     *
     * @param id           Coach ID
     * @param email        Coach email for verification
     * @param workoutPlans New list of workout plans to assign
     * @throws IllegalStateException if coach is not found
     */
    @Override
    public void updateWorkoutPlans(Long id, String email, List<String> workoutPlans) {
        CoachEntity coachEntityById = coachRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Coach with an id of: " + id + " doesnt exist"));
        CoachEntity coachEntityByEmail = coachRepository.findByEmail(email);

        if(coachEntityByEmail == null) throw new NoSuchElementException("Coach with an email of: " + email + " doesnt exist");
        if(!coachEntityById.equals(coachEntityByEmail)) throw new IllegalStateException("Coach with an email of: " + email + " isnt the same coach with an id of: " + id);

        coachEntityById.setWorkoutPlans(workoutPlans);
        coachRepository.save(coachEntityById);
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
                .orElseThrow(() -> new NoSuchElementException("Coach with an id of: " + id + " doesnt exist"));

        CoachEntity coachEntityToUpdateEmail = coachRepository.findByEmail(email);
        if(coachEntityToUpdateEmail == null) throw new NoSuchElementException("Coach with an email of: " + email + " doesnt exist");
        if(!coachEntityToUpdate.equals(coachEntityToUpdateEmail)) throw new IllegalStateException("Coach with an email of: " + email + " isnt the same coach with an id of: " + id);

        if(!coachEntityToUpdate.getEmail().equals(coachEntity.getEmail())) {
            CoachEntity coachEntity1 = coachRepository.findByEmail(coachEntity.getEmail());
            if(coachEntity1 != null) throw new IllegalArgumentException("The updated email that you are trying to give to " + coachEntityToUpdate.getName() + " is already registered under another coach");
        }

        coachEntityToUpdate.setName(coachEntity.getName());
        coachEntityToUpdate.setRole(coachEntity.getRole());
        coachEntityToUpdate.setClients(coachEntity.getClients());
        coachEntityToUpdate.setWorkoutPlans(coachEntity.getWorkoutPlans());
        coachEntityToUpdate.setDateOfBirth(coachEntity.getDateOfBirth());
        coachEntityToUpdate.setEmail(coachEntity.getEmail());
        coachRepository.save(coachEntityToUpdate);
    }

    /**
     * Updates a coach's role to MEMBER or WORKER.
     * Converts and saves new entity, clears client links, and deletes the coach.
     *
     * @param id    Coach ID
     * @param email Coach email
     * @param role  New role to assign
     * @throws IllegalStateException if credentials are invalid or role is unchanged
     */
    @Override
    public void updateRoleOfACoach(Long id, String email, String role) {
        CoachEntity coachEntityById = coachRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Coach with an id of: " + id + " doesnt exist"));
        CoachEntity coachEntityByEmail = coachRepository.findByEmail(email);

        if(coachEntityByEmail == null) throw new NoSuchElementException("Coach with an email of: " + email + " doesnt exist");
        if(!coachEntityById.equals(coachEntityByEmail)) throw new IllegalStateException("Coach with an email of: " + email + " isnt the same coach with an id of: " + id);

        if(role.equalsIgnoreCase("ROLE_COACH")) throw new IllegalArgumentException("Coach: " + coachEntityById.getName() + " already has a role of ROLE_COACH");
        else if (role.equalsIgnoreCase("ROLE_MEMBER")) {
            MemberEntity memberEntity = coachMapper.covertCoachToMemberEntity(coachEntityById);

            for(MemberEntity client : coachEntityById.getClients()) client.setCoachedBy(null);
            memberRepository.saveAll(coachEntityById.getClients());

            deleteCoachById(id);
            memberRepository.save(memberEntity);
        } else if (role.equalsIgnoreCase("ROLE_WORKER")) {
            WorkerEntity workerEntity = coachMapper.covertCoachToWorkerEntity(coachEntityById);

            for(MemberEntity client : coachEntityById.getClients()) client.setCoachedBy(null);
            memberRepository.saveAll(coachEntityById.getClients());

            deleteCoachById(id);
            workerRepository.save(workerEntity);
        }  else throw new IllegalStateException("Role must be either ROLE_COACH, ROLE_WORKER, or ROLE_MEMBER");
    }

    /**
     * Updates the coach code of a specific coach.
     *
     * @param id       ID of the coach
     * @param email    Email of the coach
     * @param coachCode  New coach code to assign
     */
    @Override
    public void updateCodeOfACoach(Long id, String email, String coachCode) {
        CoachEntity coachEntityById = coachRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Coach with an id of: " + id + " doesnt exist"));
        CoachEntity coachEntityByEmail = coachRepository.findByEmail(email);

        if(coachEntityByEmail == null) throw new NoSuchElementException("Coach with an email of: " + email + " doesnt exist");
        if(!coachEntityById.equals(coachEntityByEmail)) throw new IllegalStateException("Coach with an email of: " + email + " isnt the same coach with an id of: " + id);

        CoachEntity coachEntity = coachRepository.findByCoachCode(coachCode);
        if(coachEntity != null) throw new IllegalArgumentException("Coach with a code of: " +  coachCode + " already exists");
        coachEntityById.setCoachCode(coachCode);
        coachRepository.save(coachEntityById);
    }

    /**
     * Deletes a coach by their ID.
     *
     * @param id Coach ID
     * @throws IllegalStateException if no coach exists with the given ID
     */
    @Override
    public void deleteCoachById(Long id) {
        CoachEntity coachEntity = coachRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Coach with an id of: " + id + " doesnt exist"));

        Set<MemberEntity> memberEntities = coachEntity.getClients();
        for(MemberEntity memberEntity : memberEntities){
            memberEntity.setCoachedBy(null);
        }

        memberRepository.saveAll(memberEntities);
        coachRepository.deleteById(id);
    }

    /**
     * Deletes all coaches from the repository.
     */
    @Override
    public void deleteAllCoaches() {
        List<MemberEntity> memberEntities = memberRepository.findAll();

        for(MemberEntity memberEntity : memberEntities){
            memberEntity.setCoachedBy(null);
        }
        memberRepository.saveAll(memberEntities);
        coachRepository.deleteAll();
    }
}
