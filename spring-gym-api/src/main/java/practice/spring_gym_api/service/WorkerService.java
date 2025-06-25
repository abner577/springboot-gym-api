package practice.spring_gym_api.service;


import practice.spring_gym_api.entity.WorkerEntity;

public interface WorkerService {
    // --- GET methods ---
    WorkerEntity getWorkerById(Long id);
    WorkerEntity getWorkerByWorkerCode(String code);

    // --- POST methods ---
    void registerNewWorker(WorkerEntity workerEntity);

    // --- PUT/PATCH methods ---
    void updateRoleOfAWorker(Long id, String email, String role);
    void updateWorkerById(Long id, String email, WorkerEntity updatedWorkerEntity);

    // --- DELETE methods --- *All Delete methods need to have authentication implemented, we are looking for a heading that has a ROLE-WORKER*
    void deleteWorkerbyId(Long id);
}
