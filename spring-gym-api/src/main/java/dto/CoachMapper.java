package dto;

import entity.CoachEntity;

import java.util.List;

public interface CoachMapper {
    CoachDTO convertToCoachDto(CoachEntity coachEntity);
    CoachEntity convertToCoachEntity(CoachDTO coachDTO);
}
