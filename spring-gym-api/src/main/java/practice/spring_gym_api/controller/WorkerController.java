package practice.spring_gym_api.controller;

import practice.spring_gym_api.dto.WorkerMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

}
