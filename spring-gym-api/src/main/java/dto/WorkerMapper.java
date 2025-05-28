package dto;

import entity.WorkerEntity;

public interface WorkerMapper {
    WorkerDTO convertToWorkerDTO(WorkerEntity workerEntity);
    WorkerEntity convertToWorkerEntity(WorkerDTO workerDTO);
}
