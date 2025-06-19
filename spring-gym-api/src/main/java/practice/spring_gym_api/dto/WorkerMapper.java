package practice.spring_gym_api.dto;

import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.entity.WorkerEntity;

public interface WorkerMapper {
    WorkerDTO convertToWorkerDTO(WorkerEntity workerEntity);
    WorkerEntity convertToWorkerEntity(WorkerDTO workerDTO);
    CoachEntity covertWorkerToCoachEntity(WorkerEntity workerEntity);
    MemberEntity covertWorkerToMemberEntity(WorkerEntity workerEntity);
}
