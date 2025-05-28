package dto.impl;

import dto.MemberDTO;
import dto.MemberMapper;
import entity.MemberEntity;
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
