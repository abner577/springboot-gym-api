package practice.spring_gym_api.dto.impl;

import practice.spring_gym_api.dto.WorkerDTO;
import practice.spring_gym_api.dto.WorkerMapper;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.entity.WorkerEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import practice.spring_gym_api.entity.enums.Roles;

import java.util.List;

@Component
public class WorkerMapperimpl implements WorkerMapper {
    private final ModelMapper modelMapper;

    public WorkerMapperimpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public WorkerEntity convertToWorkerEntity(WorkerDTO workerDTO) {
        return modelMapper.map(workerDTO, WorkerEntity.class);
    }

    @Override
    public WorkerDTO convertToWorkerDTO(WorkerEntity workerEntity) {
        return modelMapper.map(workerEntity, WorkerDTO.class);
    }

    @Override
    public CoachEntity covertWorkerToCoachEntity(WorkerEntity workerEntity) {
        return new CoachEntity(workerEntity.getName(), workerEntity.getDateOfBirth(), Roles.ROLE_COACH, workerEntity.getEmail(), List.of("Placeholder workouts"), "Placeholder coach code");
    }

    @Override
    public MemberEntity covertWorkerToMemberEntity(WorkerEntity workerEntity) {
        return new MemberEntity(workerEntity.getName(), workerEntity.getDateOfBirth(), Roles.ROLE_MEMBER, workerEntity.getEmail());
    }
}
