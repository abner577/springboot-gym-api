package service.impl;

import entity.WorkerEntity;
import org.springframework.stereotype.Service;
import repository.WorkerRepository;
import service.WorkerService;

@Service
public class WorkerServiceimpl implements WorkerService {

    private final WorkerRepository workerRepository;
    public WorkerServiceimpl(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

    @Override
    public WorkerEntity getWorkerById(Long id) {
        return null;
    }

    @Override
    public void registerNewWorker(WorkerEntity workerEntity) {

    }

    @Override
    public void deleteWorkerbyId(Long id) {

    }
}
