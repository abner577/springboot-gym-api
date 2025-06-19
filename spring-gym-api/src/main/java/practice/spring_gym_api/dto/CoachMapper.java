package practice.spring_gym_api.dto;

import practice.spring_gym_api.entity.CoachEntity;
import org.mapstruct.Mapper;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.entity.WorkerEntity;


@Mapper(componentModel = "spring")
public interface CoachMapper {
    CoachDTO convertToCoachDto(CoachEntity coachEntity);
    CoachEntity convertToCoachEntity(CoachDTO coachDTO);
    MemberEntity covertCoachToMemberEntity(CoachEntity coachEntity);
    WorkerEntity covertCoachToWorkerEntity(CoachEntity coachEntity);
}
