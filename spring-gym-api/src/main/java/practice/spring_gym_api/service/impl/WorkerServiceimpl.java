package practice.spring_gym_api.service.impl;

import practice.spring_gym_api.dto.WorkerMapper;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.entity.WorkerEntity;
import org.springframework.stereotype.Service;
import practice.spring_gym_api.repository.CoachRepository;
import practice.spring_gym_api.repository.MemberRepository;
import practice.spring_gym_api.repository.WorkerRepository;
import practice.spring_gym_api.service.WorkerService;

@Service
public class WorkerServiceimpl implements WorkerService {

    private final WorkerRepository workerRepository;
    private final MemberRepository memberRepository;
    private final CoachRepository coachRepository;
    private final WorkerMapper workerMapper;


    public WorkerServiceimpl(WorkerRepository workerRepository, MemberRepository memberRepository, CoachRepository coachRepository, WorkerMapper workerMapper) {
        this.workerRepository = workerRepository;
        this.memberRepository = memberRepository;
        this.coachRepository = coachRepository;
        this.workerMapper = workerMapper;
    }

    /**
     * Retrieves a worker by their ID.
     *
     * @param id Worker ID
     * @return WorkerEntity object
     * @throws IllegalStateException if worker is not found
     */
    @Override
    public WorkerEntity getWorkerById(Long id) {
        return workerRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Worker with an id of: " + id + " doesnt exist"));

    }

    @Override
    public WorkerEntity getWorkerByWorkerCode(String code) {
        return null;
    }

    /**
     * Adds a new worker to the system.
     *
     * @param workerEntity Worker entity to save
     */
    @Override
    public void registerNewWorker(WorkerEntity workerEntity) {
        WorkerEntity workerEntity1 = workerRepository.findByEmail(workerEntity.getEmail());
        if(workerEntity1 != null) throw new IllegalStateException("Worker with an email of: " + workerEntity.getEmail() + " already exists");

        workerRepository.save(workerEntity);
    }

    /**
     * Updates the role of a worker to a new role (member or coach).
     * Deletes the worker and converts to the new entity type.
     *
     * @param id    Worker ID
     * @param email Worker email
     * @param role  New role to assign (ROLE_MEMBER or ROLE_COACH)
     * @throws IllegalStateException if credentials are invalid or role is unchanged/invalid
     */
    @Override
    public void updateRoleOfAWorker(Long id, String email, String role) {
        WorkerEntity workerEntityById = workerRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Worker with an id of: " + id + " doesnt exist"));
        WorkerEntity workerEntityByEmail = workerRepository.findByEmail(email);

        if(workerEntityByEmail == null) throw new IllegalStateException("Worker with an email of: " + workerEntityById.getEmail() + " doesnt exist");
        if(!workerEntityById.equals(workerEntityByEmail)) throw new IllegalStateException("Worker with an email of: " + workerEntityById.getEmail() + " is not the same worker with an id of: " + workerEntityById.getId());


        if(role.equalsIgnoreCase("ROLE_WORKER ")) throw new IllegalStateException("Coach: " + workerEntityById.getName() + " already has a role of ROLE_WORKER");
        else if (role.equalsIgnoreCase("ROLE_MEMBER")) {
            MemberEntity memberEntity = workerMapper.covertWorkerToMemberEntity(workerEntityById);

            deleteWorkerbyId(id);
            memberRepository.save(memberEntity);
        } else if (role.equalsIgnoreCase("ROLE_COACH")) {
            CoachEntity coachEntity = workerMapper.covertWorkerToCoachEntity(workerEntityById);

            deleteWorkerbyId(id);
            coachRepository.save(coachEntity);
        }  else throw new IllegalStateException("Role must be either ROLE_COACH, ROLE_WORKER, or ROLE_MEMBER");
    }

    /**
     * Updates a worker's details by ID and email.
     * Verifies credentials and email uniqueness before updating fields.
     *
     * @param id                Worker ID
     * @param email             Current worker email
     * @param updatedWorkerEntity Updated worker data
     * @throws IllegalStateException if credentials are invalid or email is already in use
     */
    @Override
    public void updateWorkerById(Long id, String email, WorkerEntity updatedWorkerEntity) {
        WorkerEntity workerEntity = workerRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Worker with an id of: " + id + " doesnt exist"));
        WorkerEntity workerEntity1 = workerRepository.findByEmail(email);

        if(workerEntity1 == null) throw new IllegalStateException("Worker with an email of: " + workerEntity.getEmail() + " doesnt exist");
        if(!workerEntity.equals(workerEntity1)) throw new IllegalStateException("Worker with an email of: " + workerEntity.getEmail() + " is not the same worker with an id of: " + workerEntity.getId());

        if(!workerEntity.getEmail().equals(updatedWorkerEntity.getEmail())) {
            WorkerEntity workerEntity2 = workerRepository.findByEmail(updatedWorkerEntity.getEmail());
            if(workerEntity2 != null) throw new IllegalStateException("The updated email that you are trying to give to " + workerEntity.getName() + " is already registered under another worker");
        }

        workerEntity.setDateOfBirth(updatedWorkerEntity.getDateOfBirth());
        workerEntity.setEmail(updatedWorkerEntity.getEmail());
        workerEntity.setName(updatedWorkerEntity.getName());
        workerEntity.setRole(updatedWorkerEntity.getRole());
        workerRepository.save(workerEntity);
    }

    /**
     * Deletes a worker by their ID.
     *
     * @param id Worker ID
     * @throws IllegalStateException if worker is not found
     */
    @Override
    public void deleteWorkerbyId(Long id) {
        WorkerEntity workerEntity = workerRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Worker with an id of: " + id + " doesnt exist"));
        workerRepository.delete(workerEntity);
    }
}
