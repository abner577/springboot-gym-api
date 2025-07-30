package practice.spring_gym_api.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.spring_gym_api.dto.WorkerDTO;
import practice.spring_gym_api.dto.WorkerMapper;
import practice.spring_gym_api.entity.WorkerEntity;
import practice.spring_gym_api.service.WorkerService;

@RestController
@RequestMapping(path = "api/v1/gym-api")
public class WorkerController {

    private final WorkerService workerService;
    private final WorkerMapper workerMapper;

    public WorkerController(WorkerService workerService, WorkerMapper workerMapper) {
        this.workerService = workerService;
        this.workerMapper= workerMapper;
    }

    /**
     * Retrieves a worker by their ID.
     *
     * @param id Worker ID
     * @return WorkerDTO representing the worker
     */
    @GetMapping(path = "workers/{worker_id}")
    public WorkerDTO getWorkerByID(@PathVariable("worker_id") Long id){
        WorkerEntity workerEntity = workerService.getWorkerById(id);
        return workerMapper.convertToWorkerDTO(workerEntity);
    }

    /**
     * Retrieves a worker by their ID and worker code.
     *
     * @param id    ID of the worker
     * @param code  Worker code to verify
     * @return      Matching WorkerDTO
     */
    @GetMapping(path = "workers/{worker_id}/code")
    public WorkerDTO getWorkerByCode(
            @PathVariable("worker_id") Long id,
            @RequestParam String code
    ) {
        WorkerEntity workerEntity = workerService.getWorkerByWorkerCode(id, code);
        return workerMapper.convertToWorkerDTO(workerEntity);
    }

    /**
     * Registers a new worker in the system.
     *
     * @param workerEntity WorkerEntity object to be registered
     */
    @PostMapping (path = "workers")
    public void registerNewWorker(@Valid @RequestBody WorkerEntity workerEntity) {
        workerService.registerNewWorker(workerEntity);
    }

    /**
     * Updates the role of a worker by ID and email.
     *
     * @param id    Worker ID
     * @param email Worker email
     * @param role  New role to assign
     */
    @PatchMapping (path = "workers/{worker_id}/role")
    public void updateRoleOfAWorker(
            @PathVariable("worker_id") Long id,
            @RequestParam String email,
            @RequestParam String role
    ){
        workerService.updateRoleOfAWorker(id, email, role);
    }

    /**
     * Updates the worker code of a specific worker by ID.
     *
     * @param id    ID of the worker
     * @param email Email of the worker for verification
     * @param code  New worker code to assign
     */
    @PatchMapping (path = "workers/{worker_id}/code")
    public void updateWorkerCodeById(
            @PathVariable("worker_id") Long id,
            @RequestParam String email,
            @RequestParam String code
    ){
        workerService.updateWorkerCodeById(id, email, code);
    }

    /**
     * Updates a worker's details by ID.
     *
     * @param id            ID of the worker to update
     * @param email         Email of the worker for verification
     * @param workerEntity  Updated worker data
     */
    @PutMapping (path = "workers/{worker_id}")
    public void updatedWorkerById(
            @PathVariable("worker_id") Long id,
            @RequestParam String email,
            @Valid @RequestBody WorkerEntity workerEntity
    ) {
        workerService.updateWorkerById(id, email, workerEntity);
    }

    /**
     * Deletes a worker from the system by ID.
     *
     * @param id Worker ID
     */
    @DeleteMapping (path = "workers/{worker_id}")
    public void deleteWorkerByID(@PathVariable("worker_id") Long id){
        workerService.deleteWorkerbyId(id);
    }

}
