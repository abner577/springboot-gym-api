package practice.spring_gym_api.service;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import practice.spring_gym_api.entity.MemberEntity;

import java.util.List;
import java.util.Set;

public interface MemberService {
    // --- GET methods ---
    MemberEntity getMemberById(Long id);
    Page<MemberEntity> getAllMembers(Pageable pageable);
    MemberEntity getMemberByHighestBench();
    MemberEntity getMemberByHighestSquat();
    MemberEntity getMemberByHighestDeadlift();
    MemberEntity getMemberByHighestTotal();
    List<MemberEntity> getAllMembersAboveATotal(int total);
    List<MemberEntity> getAllAvaliableMembers();

    // --- POST methods ---
    void registerNewMember(MemberEntity memberEntity);
    void registerNewMembers(List<MemberEntity> memberEntities);

    // --- PUT/PATCH methods ---
    void updateCoachedByById(Long id, Long coachID);
    void updateNameByIdAndEmail(Long id, String name, String email);
    void updateMultipleMembersNameByIdAndEmail(List<Long> ids, List<String> names, List<String> emails);
    void updateSBDStatus(Long id, String email, int bench, int squat, int deadlift); //make these requestParams, so make them optional, any one of them
    void updateCompleteMember(Long id, String email, MemberEntity memberEntity);

    // --- DELETE methods --- *All Delete methods need to have authentication implemented, we are lookign for a heading that has a ROLE-WORKER*
    void deleteMemberById(Long id, Long coachID);
    void deleteMembersBelowATotal(int total);
}
