package dto;

import entity.CoachEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface CoachMapper {
    CoachDTO convertToCoachDto(CoachEntity coachEntity);
    CoachEntity convertToCoachEntity(CoachDTO coachDTO);
}
