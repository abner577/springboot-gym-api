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

    @GetMapping(path = "worker/{worker_id}")
    public WorkerDTO getWorkerByID(@PathVariable("worker_id") Long id){
        WorkerEntity workerEntity = workerService.getWorkerById(id);
        return workerMapper.convertToWorkerDTO(workerEntity);
    }

    @PostMapping (path = "worker")
    public void registerNewWorker(@Valid @RequestBody WorkerEntity workerEntity) {
        workerService.registerNewWorker(workerEntity);
    }

    @DeleteMapping (path = "worker/{worker_id}")
    public void deleteWorkerByID(@PathVariable("worker_id") Long id){
        workerService.deleteWorkerbyId(id);
    }

}
