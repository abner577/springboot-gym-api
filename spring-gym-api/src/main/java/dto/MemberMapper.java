package dto;

import entity.MemberEntity;

public interface MemberMapper {
    MemberDTO converToMemberDTO(MemberEntity memberEntity);
    MemberEntity convertToMemberEntity(MemberDTO memberDTO);
}
