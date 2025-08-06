package practice.spring_gym_api.service.impl;

import practice.spring_gym_api.dto.WorkerMapper;
import practice.spring_gym_api.dto.request.WorkerRequestDTO;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.entity.WorkerEntity;
import org.springframework.stereotype.Service;
import practice.spring_gym_api.repository.CoachRepository;
import practice.spring_gym_api.repository.MemberRepository;
import practice.spring_gym_api.repository.WorkerRepository;
import practice.spring_gym_api.service.WorkerService;

import java.util.NoSuchElementException;
import java.util.Objects;

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
                .orElseThrow(() -> new NoSuchElementException("Worker with an id of: " + id + " doesnt exist"));

    }

    /**
     * Retrieves a worker by their ID and worker code.
     *
     * @param id    ID of the worker
     * @param code  Worker code to verify
     * @return      Matching WorkerEntity
     */
    @Override
    public WorkerEntity getWorkerByWorkerCode(Long id, String code) {
        WorkerEntity workerEntity = workerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Worker with an id of: " + id + " doesnt exist"));

        if(code == null || code.isEmpty()) throw new IllegalArgumentException("Code cannot be null or empty");

        WorkerEntity workerEntity1 = workerRepository.findByWorkerCode(code);
        if(workerEntity1 == null) throw new NoSuchElementException("Worker with a worker code of: " + code + " doesnt exist");

        if(!workerEntity.getWorkerCode().equals(workerEntity1.getWorkerCode())){
            throw new IllegalStateException("Worker with an id of: " + id + " is not the same worker with a worker code of: " + code);
        }
        return workerEntity1;
    }

    /**
     * Adds a new worker to the system.
     *
     * @param workerRequestDTO Worker entity to save
     */
    @Override
    public void registerNewWorker(WorkerRequestDTO workerRequestDTO) {
        WorkerEntity workerEntity1 = workerRepository.findByEmail(workerRequestDTO.getEmail());
        if(workerEntity1 != null) throw new IllegalStateException("Worker with an email of: " + workerRequestDTO.getEmail() + " already exists");
        if(!Objects.equals(workerRequestDTO.getWorkerCode(), "Placeholder worker code")) throw new IllegalArgumentException("Initial worker code must be: 'Placeholder worker code'");

        WorkerEntity workerEntity = workerMapper.convertToWorkerEntity(workerRequestDTO);
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
                .orElseThrow(() -> new NoSuchElementException("Worker with an id of: " + id + " doesnt exist"));
        WorkerEntity workerEntityByEmail = workerRepository.findByEmail(email);

        if(email == null || email.isEmpty()) throw new IllegalArgumentException("Email cannot be null or empty");
        if(role == null || role.isEmpty()) throw new IllegalArgumentException("Role cannot be null or empty");

        if(workerEntityByEmail == null) throw new NoSuchElementException("Worker with an email of: " + workerEntityById.getEmail() + " doesnt exist");
        if(!workerEntityById.equals(workerEntityByEmail)) throw new IllegalStateException("Worker with an email of: " + workerEntityById.getEmail() + " is not the same worker with an id of: " + workerEntityById.getId());


        if(role.equalsIgnoreCase("ROLE_WORKER")) throw new IllegalStateException("Coach: " + workerEntityById.getName() + " already has a role of ROLE_WORKER");
        else if (role.equalsIgnoreCase("ROLE_MEMBER")) {
            MemberEntity memberEntity = workerMapper.covertWorkerToMemberEntity(workerEntityById);

            deleteWorkerbyId(id);
            memberRepository.save(memberEntity);
        } else if (role.equalsIgnoreCase("ROLE_COACH")) {
            CoachEntity coachEntity = workerMapper.covertWorkerToCoachEntity(workerEntityById);

            deleteWorkerbyId(id);
            coachRepository.save(coachEntity);
        }  else throw new IllegalArgumentException("Role must be either ROLE_COACH, ROLE_WORKER, or ROLE_MEMBER");
    }

    /**
     * Updates a worker's details by ID and email.
     * Verifies credentials and email uniqueness before updating fields.
     *
     * @param id                Worker ID
     * @param email             Current worker email
     * @param workerRequestDTO Updated worker data
     * @throws IllegalStateException if credentials are invalid or email is already in use
     */
    @Override
    public void updateWorkerById(Long id, String email, WorkerRequestDTO workerRequestDTO) {
        WorkerEntity workerEntity = workerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Worker with an id of: " + id + " doesnt exist"));
        if(email == null || email.isEmpty()) throw new IllegalArgumentException("Email cannot be null or empty");

        WorkerEntity workerEntity1 = workerRepository.findByEmail(email);

        if(workerEntity1 == null) throw new NoSuchElementException("Worker with an email of: " + workerEntity.getEmail() + " doesnt exist");
        if(!workerEntity.equals(workerEntity1)) throw new IllegalStateException("Worker with an email of: " + workerEntity.getEmail() + " is not the same worker with an id of: " + workerEntity.getId());

        // if you are trying to update email
        if(!Objects.equals(workerEntity.getEmail(), workerRequestDTO.getEmail())) {
            WorkerEntity workerEntity2 = workerRepository.findByEmail(workerRequestDTO.getEmail());
            if(workerEntity2 != null) throw new IllegalStateException("The updated email that you are trying to give to " + workerEntity.getName() + " is already registered under another worker");
        }

        if(!Objects.equals(workerRequestDTO.getRole(), "ROLE_WORKER")) throw new IllegalStateException("role must be 'ROLE_WORKER'");

       if(workerRepository.findByWorkerCode(workerRequestDTO.getWorkerCode()) != null) {
           throw new IllegalStateException("The updated worker code that you are trying to give to " + workerEntity.getName() + " is already registered under another worker");
       }

        workerEntity.setDateOfBirth(workerRequestDTO.getDateOfBirth());
        workerEntity.setEmail(workerRequestDTO.getEmail());
        workerEntity.setName(workerRequestDTO.getName());
        workerEntity.setWorkerCode(workerRequestDTO.getWorkerCode());
        workerRepository.save(workerEntity);
    }

    /**
     * Updates the worker code of a specific worker.
     *
     * @param id       ID of the worker
     * @param email    Email of the worker
     * @param newCode  New worker code to assign
     */
    @Override
    public void updateWorkerCodeById(Long id, String email, String newCode) {
        WorkerEntity workerEntity = workerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Worker with an id of: " + id + " doesnt exist"));

        if(email == null || email.isEmpty()) throw new IllegalArgumentException("Email cannot be null or empty");
        if(newCode == null || newCode.isEmpty()) throw new IllegalArgumentException("Worker code cannot be null or empty");

        WorkerEntity workerEntity1 = workerRepository.findByEmail(email);
        if(workerEntity1 == null) throw new NoSuchElementException("Worker with an email of: " + workerEntity.getEmail() + " doesnt exist");
        if(!workerEntity.equals(workerEntity1)) throw new IllegalStateException("Worker with an email of: " + workerEntity.getEmail() + " is not the same worker with an id of: " + workerEntity.getId());

        if(workerRepository.findByWorkerCode(newCode) != null) {
            throw new IllegalStateException("The updated worker code that you are trying to give to: " + workerEntity.getName() + " is already registered under another worker");
        }

        workerEntity.setWorkerCode(newCode);
        workerRepository.save(workerEntity);
    }

    /**
     * Deletes a worker by their ID.
     *
     * @param id Worker ID
     * @throws IllegalStateException if the worker is not found
     */
    @Override
    public void deleteWorkerbyId(Long id) {
        WorkerEntity workerEntity = workerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Worker with an id of: " + id + " doesnt exist"));
        workerRepository.delete(workerEntity);
    }
}
