package practice.spring_gym_api.dto.impl;

import practice.spring_gym_api.dto.MemberDTO;
import practice.spring_gym_api.dto.MemberMapper;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import practice.spring_gym_api.entity.WorkerEntity;
import practice.spring_gym_api.entity.enums.Roles;

import java.util.List;

@Component
public class MemberMapperimpl implements MemberMapper {
    private final ModelMapper modelMapper;

    public MemberMapperimpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public MemberEntity convertToMemberEntity(MemberDTO memberDTO) {
        return modelMapper.map(memberDTO, MemberEntity.class);
    }

    @Override
    public MemberDTO convertToMemberDTO(MemberEntity memberEntity) {
        return modelMapper.map(memberEntity, MemberDTO.class);
    }

    @Override
    public CoachEntity convertMemberToCoachEntity (MemberEntity memberEntity){
        return new CoachEntity(memberEntity.getName(), memberEntity.getDateOfBirth(), Roles.ROLE_COACH, memberEntity.getEmail(), List.of("Placeholder workouts"), "Placeholder coach code");
    }

    @Override
    public WorkerEntity convertMemberToWorkerEntity (MemberEntity memberEntity){
        return new WorkerEntity(memberEntity.getName(), memberEntity.getDateOfBirth(), Roles.ROLE_WORKER, memberEntity.getEmail(), "Placeholder worker code");
    }
}
