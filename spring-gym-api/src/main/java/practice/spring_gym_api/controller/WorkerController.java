package practice.spring_gym_api.controller;

import jakarta.validation.Valid;
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
    @GetMapping(path = "worker/{worker_id}")
    public WorkerDTO getWorkerByID(@PathVariable("worker_id") Long id){
        WorkerEntity workerEntity = workerService.getWorkerById(id);
        return workerMapper.convertToWorkerDTO(workerEntity);
    }

    /**
     * Registers a new worker in the system.
     *
     * @param workerEntity WorkerEntity object to be registered
     */
    @PostMapping (path = "worker")
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
    @PatchMapping (path = "update/worker/role/{worker_id}")
    public void updateRoleOfAWorker(
            @PathVariable("worker_id") Long id,
            @RequestParam String email,
            @RequestParam String role
    ){
        workerService.updateRoleOfAWorker(id, email, role);
    }

    /**
     * Deletes a worker from the system by ID.
     *
     * @param id Worker ID
     */
    @DeleteMapping (path = "worker/{worker_id}")
    public void deleteWorkerByID(@PathVariable("worker_id") Long id){
        workerService.deleteWorkerbyId(id);
    }

}
