package practice.spring_gym_api.service;



import practice.spring_gym_api.entity.MemberEntity;

import java.util.List;

public interface MemberService {
    // --- GET methods ---
    MemberEntity getMemberById(Long id);
    List<MemberEntity> getAllMembers();
    MemberEntity getMemberByHighestBench();
    MemberEntity getMemberByHighestSquat();
    MemberEntity getMemberByHighestDeadlift();
    MemberEntity getMemberByHighestTotal();
    MemberEntity getMemberAboveATotal(int total);

    // --- POST methods ---
    void registerNewMember(MemberEntity memberEntity);
    void registerNewMembers(List<MemberEntity> memberEntities);

    // --- PUT/PATCH methods ---
    void updateNameAndEmailById(Long id, String name, String email);
    void updateSBDStatus(Long id, int bench, int squat, int deadlift); //make these requestParams, so make them optional, any one of them

    // --- DELETE methods --- *All Delete methods need to have authentication implemented, we are lookign for a heading that has a ROLE-WORKER*
    void deleteMemberById(Long id);
    void deleteMembersBelowATotal(int total);
}
