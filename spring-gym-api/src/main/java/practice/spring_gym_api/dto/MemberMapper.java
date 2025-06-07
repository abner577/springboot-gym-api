package practice.spring_gym_api.dto;

import practice.spring_gym_api.entity.MemberEntity;

public interface MemberMapper {
    MemberDTO convertToMemberDTO(MemberEntity memberEntity);
    MemberEntity convertToMemberEntity(MemberDTO memberDTO);
}
