package service;


import entity.WorkerEntity;

import java.util.List;

public interface WorkerService {
    // --- GET methods ---
    WorkerEntity getWorkerById(Long id);

    // --- POST methods ---
    void registerNewWorker(WorkerEntity workerEntity);

    // --- PUT/PATCH methods ---

    // --- DELETE methods --- *All Delete methods need to have authentication implemented, we are lookign for a heading that has a ROLE-WORKER*
    void deleteWorkerbyId(Long id);
}
