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
        return workerRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Worker with an id of: " + id + " doesnt exist"));

    }

    @Override
    public void registerNewWorker(WorkerEntity workerEntity) {
        if(workerRepository.existsByEmail(workerEntity.getEmail())) throw new IllegalStateException("Worker with an email of: " + workerEntity.getEmail() + " already exists");
        workerRepository.save(workerEntity);
    }

    @Override
    public void deleteWorkerbyId(Long id) {
        WorkerEntity workerEntity = workerRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Worker with an id of: " + id + " doesnt exist"));
        workerRepository.delete(workerEntity);
    }
}
