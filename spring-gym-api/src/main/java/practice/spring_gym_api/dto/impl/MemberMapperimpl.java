package practice.spring_gym_api.dto.impl;

import practice.spring_gym_api.dto.MemberDTO;
import practice.spring_gym_api.dto.MemberMapper;
import practice.spring_gym_api.entity.MemberEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

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
}
