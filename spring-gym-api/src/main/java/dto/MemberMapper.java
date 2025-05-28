package dto;

import entity.MemberEntity;

public interface MemberMapper {
    MemberDTO convertToMemberDTO(MemberEntity memberEntity);
    MemberEntity convertToMemberEntity(MemberDTO memberDTO);
}
