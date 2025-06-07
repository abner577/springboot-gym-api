package practice.spring_gym_api.service.impl;

import practice.spring_gym_api.entity.WorkerEntity;
import org.springframework.stereotype.Service;
import practice.spring_gym_api.repository.WorkerRepository;
import practice.spring_gym_api.service.WorkerService;

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
