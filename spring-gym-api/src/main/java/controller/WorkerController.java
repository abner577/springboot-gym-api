package controller;

import dto.WorkerMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.WorkerService;

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
