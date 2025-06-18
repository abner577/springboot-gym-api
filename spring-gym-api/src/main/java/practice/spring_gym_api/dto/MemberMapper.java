package practice.spring_gym_api.dto;

import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.entity.WorkerEntity;

public interface MemberMapper {
    MemberDTO convertToMemberDTO(MemberEntity memberEntity);
    MemberEntity convertToMemberEntity(MemberDTO memberDTO);
    CoachEntity convertMemberToCoachEntity(MemberEntity memberEntity);
    WorkerEntity convertMemberToWorkerEntity(MemberEntity memberEntity);
}
