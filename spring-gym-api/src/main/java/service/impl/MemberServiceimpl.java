package service.impl;

import entity.MemberEntity;
import org.springframework.stereotype.Service;
import repository.MemberRepository;
import service.MemberService;

import java.util.List;

@Service
public class MemberServiceimpl implements MemberService {

    private final MemberRepository memberRepository;
    public MemberServiceimpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public MemberEntity getMemberById(Long id) {
        return null;
    }

    @Override
    public List<MemberEntity> getAllMembers() {
        return List.of();
    }

    @Override
    public MemberEntity getMemberByHighestBench() {
        return null;
    }

    @Override
    public MemberEntity getMemberByHighestSquat() {
        return null;
    }

    @Override
    public MemberEntity getMemberByHighestDeadlift() {
        return null;
    }

    @Override
    public MemberEntity getMemberByHighestTotal() {
        return null;
    }

    @Override
    public MemberEntity getMemberAboveATotal(int total) {
        return null;
    }

    @Override
    public void registerNewMember(MemberEntity memberEntity) {

    }

    @Override
    public void registerNewMembers(List<MemberEntity> memberEntities) {

    }

    @Override
    public void updateNameAndEmailById(Long id, String name, String email) {

    }

    @Override
    public void updateSBDStatus(Long id, int bench, int squat, int deadlift) {

    }

    @Override
    public void deleteMemberById(Long id) {

    }

    @Override
    public void deleteMembersBelowATotal(int total) {

    }
}
