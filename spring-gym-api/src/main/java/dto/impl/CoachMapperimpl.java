package dto.impl;

import dto.CoachDTO;
import dto.CoachMapper;
import entity.CoachEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

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
    public CoachEntity convertToCoachEntity(CoachDTO coachDTO) {
        return modelMapper.map(coachDTO, CoachEntity.class);
    }
}
