package practice.spring_gym_api.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import practice.spring_gym_api.entity.MemberEntity;
import org.springframework.stereotype.Service;
import practice.spring_gym_api.repository.MemberRepository;
import practice.spring_gym_api.service.MemberService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MemberServiceimpl implements MemberService {

    private final MemberRepository memberRepository;
    public MemberServiceimpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public MemberEntity getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Member with an id of: " + id + " doesnt exist"));
    }

    @Override
    public Page<MemberEntity> getAllMembers(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    @Override
    public MemberEntity getMemberByHighestBench() {
        List<MemberEntity> listOfAllEntities = memberRepository.findAll();
        double maxBench = 0;
        MemberEntity entityWithMax = listOfAllEntities.getFirst();

        for(MemberEntity memberEntity : listOfAllEntities){
            if(memberEntity.getBench() > maxBench) {
                maxBench = memberEntity.getBench();
                entityWithMax = memberEntity;
            }
        }
        return entityWithMax;
    }

    @Override
    public MemberEntity getMemberByHighestSquat() {
        List<MemberEntity> listOfAllEntities = memberRepository.findAll();
        double maxSquat = 0;
        MemberEntity entityWithMax = listOfAllEntities.getFirst();

        for(MemberEntity memberEntity : listOfAllEntities){
            if(memberEntity.getSquat() > maxSquat) {
                maxSquat = memberEntity.getSquat();
                entityWithMax = memberEntity;
            }
        }
        return entityWithMax;
    }

    @Override
    public MemberEntity getMemberByHighestDeadlift() {
        List<MemberEntity> listOfAllEntities = memberRepository.findAll();
        double maxDeadlift = 0;
        MemberEntity entityWithMax = listOfAllEntities.getFirst();

        for(MemberEntity memberEntity : listOfAllEntities){
            if(memberEntity.getDeadlift() > maxDeadlift) {
                maxDeadlift = memberEntity.getDeadlift();
                entityWithMax = memberEntity;
            }
        }
        return entityWithMax;
    }

    @Override
    public MemberEntity getMemberByHighestTotal() {
        List<MemberEntity> listOfAllEntities = memberRepository.findAll();
        double maxTotal = 0;
        MemberEntity entityWithMax = listOfAllEntities.getFirst();

        for(MemberEntity memberEntity : listOfAllEntities){
            if(memberEntity.getTotal() > maxTotal) {
                maxTotal = memberEntity.getTotal();
                entityWithMax = memberEntity;
            }
        }
        return entityWithMax;
    }

    @Override
    public List<MemberEntity> getAllMembersAboveATotal(int total) {
        List<MemberEntity> listOfAllEntities = memberRepository.findAll();
        List<MemberEntity> listOfEntitiesToReturn = new ArrayList<>();

        for(MemberEntity memberEntity : listOfAllEntities){
            if(memberEntity.getTotal() > total) listOfEntitiesToReturn.add(memberEntity);
        }
        return listOfEntitiesToReturn;
    }

    @Override
    public void registerNewMember(MemberEntity memberEntity) {
        if(memberRepository.findMemberByName(memberEntity.getName())) throw new IllegalStateException("Member with a name of: " + memberEntity.getName() + " already exists");
        memberRepository.save(memberEntity);
    }

    @Override
    public void registerNewMembers(List<MemberEntity> memberEntities) {
        for(MemberEntity memberEntity : memberEntities){
            if(memberRepository.findMemberByName(memberEntity.getName())) throw new IllegalStateException("Member with a name of: " + memberEntity.getName() + " already exists");
        }
        memberRepository.saveAll(memberEntities);
    }

    @Override
    public void updateNameById(Long id, String name) {
        MemberEntity memberEntityToUpdate = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Member with an id of: " + id + " doesnt exist"));
        if(memberRepository.findMemberByName(name)) throw new IllegalStateException("Member with a name of: " + name + " already exists");

        if(name != null && name.length() > 0) {
            memberEntityToUpdate.setName(name);
            memberRepository.save(memberEntityToUpdate);
        } else throw new IllegalStateException("Name provided must be not-null and must not be an empty string");
    }

    @Override
    public void updateMultipleMembersNameById(List<Long> ids, List<String> names) {
        if(ids.size() != names.size()) throw new IllegalStateException("Size of names and id's lists must be equal");
        Set<Long> idsCopy = new HashSet<>();

        for(Long id : ids){
            idsCopy.add(id);
            memberRepository.findById(id)
                    .orElseThrow(() -> new IllegalStateException("Member with an id of: " + id + " doesnt exist"));
        }
        if(idsCopy.size() != ids.size()) throw new IllegalStateException("Duplicates detected inside of id list, each id must be unique");

        List<MemberEntity> memberEntitiesToUpdate = memberRepository.findAllById(ids);

        for(int i =0; i < memberEntitiesToUpdate.size(); i++){
            if(names.get(i) == null || names.get(i).isEmpty() || names.get(i).length() < 0) throw new IllegalStateException("Name provided must be not-null and must not be an empty string");

            if(memberRepository.findMemberByName(names.get(i)))
                throw new IllegalStateException("Member with a name of: " + memberEntitiesToUpdate.get(i).getName() + " already exists");
            memberEntitiesToUpdate.get(i).setName(names.get(i));
        }
        memberRepository.saveAll(memberEntitiesToUpdate);
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
