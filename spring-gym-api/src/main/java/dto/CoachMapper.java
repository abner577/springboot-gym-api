package dto;

import entity.CoachEntity;

public interface CoachMapper {
    CoachDTO convertToCoachDto(CoachEntity coachEntity);
    CoachEntity convertToCoachEntity(CoachDTO coachDTO);
}
