package practice.spring_gym_api.dto;

import practice.spring_gym_api.dto.request.WorkerRequestDTO;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.entity.WorkerEntity;

public interface WorkerMapper {
    WorkerDTO convertToWorkerDTO(WorkerEntity workerEntity);
    WorkerEntity convertToWorkerEntity(WorkerRequestDTO workerRequestDTO);
    CoachEntity covertWorkerToCoachEntity(WorkerEntity workerEntity);
    MemberEntity covertWorkerToMemberEntity(WorkerEntity workerEntity);
}
