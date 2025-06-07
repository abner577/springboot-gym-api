package practice.spring_gym_api.dto;

import practice.spring_gym_api.entity.WorkerEntity;

public interface WorkerMapper {
    WorkerDTO convertToWorkerDTO(WorkerEntity workerEntity);
    WorkerEntity convertToWorkerEntity(WorkerDTO workerDTO);
}
