package practice.spring_gym_api.dto.impl;

import practice.spring_gym_api.dto.CoachDTO;
import practice.spring_gym_api.dto.CoachMapper;
import practice.spring_gym_api.dto.MemberDTO;
import practice.spring_gym_api.dto.request.CoachRequestDTO;
import practice.spring_gym_api.entity.CoachEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.entity.WorkerEntity;
import practice.spring_gym_api.entity.enums.Roles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class CoachMapperimpl implements CoachMapper {
    private final ModelMapper modelMapper;

    public CoachMapperimpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public CoachDTO convertToCoachDto(CoachEntity coachEntity) {
       return modelMapper.map(coachEntity, CoachDTO.class);
    }

    @Override
    public CoachEntity convertToCoachEntity(CoachRequestDTO coachRequestDTO) {
        return new CoachEntity(
                coachRequestDTO.getName(), coachRequestDTO.getDateOfBirth(),
                coachRequestDTO.getRole(), coachRequestDTO.getEmail(),
                coachRequestDTO.getWorkoutPlans(), "Placeholder coach code"
        );
    }

    @Override
    public MemberEntity covertCoachToMemberEntity(CoachEntity coachEntity) {
        return new MemberEntity(
                coachEntity.getName(), coachEntity.getDateOfBirth(),
                LocalDate.now().toString(),
                coachEntity.getEmail(), Roles.ROLE_MEMBER,
                0, 0, 0, 0
        );
    }

    @Override
    public WorkerEntity covertCoachToWorkerEntity(CoachEntity coachEntity) {
        return new WorkerEntity(coachEntity.getName(), coachEntity.getDateOfBirth(), Roles.ROLE_WORKER, coachEntity.getEmail(), "Placeholder worker code");
    }
}
