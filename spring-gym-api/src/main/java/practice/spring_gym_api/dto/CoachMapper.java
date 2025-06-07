package practice.spring_gym_api.dto;

import practice.spring_gym_api.entity.CoachEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface CoachMapper {
    CoachDTO convertToCoachDto(CoachEntity coachEntity);
    CoachEntity convertToCoachEntity(CoachDTO coachDTO);
}
