package practice.spring_gym_api.dto.impl;

import practice.spring_gym_api.dto.WorkerDTO;
import practice.spring_gym_api.dto.WorkerMapper;
import practice.spring_gym_api.dto.request.WorkerRequestDTO;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.entity.WorkerEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import practice.spring_gym_api.entity.enums.Roles;

import java.time.LocalDate;
import java.util.List;

@Component
public class WorkerMapperimpl implements WorkerMapper {
    private final ModelMapper modelMapper;

    public WorkerMapperimpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public WorkerEntity convertToWorkerEntity(WorkerRequestDTO workerRequestDTO) {
        return new WorkerEntity(
                workerRequestDTO.getName(), workerRequestDTO.getDateOfBirth(), workerRequestDTO.getRole(),
                workerRequestDTO.getEmail(), workerRequestDTO.getWorkerCode()
        );
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
        return new MemberEntity(
                workerEntity.getName(), workerEntity.getDateOfBirth(),
                LocalDate.now().toString(),
                workerEntity.getEmail(), Roles.ROLE_MEMBER,
                0, 0, 0, 0
        );
    }
}
