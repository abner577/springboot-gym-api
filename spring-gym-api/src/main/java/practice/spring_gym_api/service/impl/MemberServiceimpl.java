package practice.spring_gym_api.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import org.springframework.stereotype.Service;
import practice.spring_gym_api.repository.CoachRepository;
import practice.spring_gym_api.repository.MemberRepository;
import practice.spring_gym_api.service.MemberService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service implementation for managing Member-related operations.
 * Handles business logic and interacts with the MemberRepository.
 */
@Service
public class MemberServiceimpl implements MemberService {

    private final MemberRepository memberRepository;
    private final CoachRepository coachRepository;

    public MemberServiceimpl(MemberRepository memberRepository, CoachRepository coachRepository) {
        this.memberRepository = memberRepository;
        this.coachRepository = coachRepository;
    }

    /**
     * Retrieves a member by ID.
     *
     * @param id Member ID
     * @return MemberEntity if found
     * @throws IllegalStateException if no member exists with the given ID
     */
    @Override
    public MemberEntity getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Member with an id of: " + id + " doesnt exist"));
    }

    /**
     * Retrieves a paginated list of all members.
     *
     * @param pageable Pagination settings
     * @return Page of MemberEntity
     */
    @Override
    public Page<MemberEntity> getAllMembers(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    /**
     * Retrieves the member with the highest bench press.
     *
     * @return MemberEntity with highest bench
     */
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

    /**
     * Retrieves the member with the highest squat.
     *
     * @return MemberEntity with highest squat
     */
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

    /**
     * Retrieves the member with the highest deadlift.
     *
     * @return MemberEntity with highest deadlift
     */
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

    /**
     * Retrieves the member with the highest total (bench + squat + deadlift).
     *
     * @return MemberEntity with highest total
     */
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

    /**
     * Retrieves all members whose total is greater than the specified value.
     *
     * @param total The threshold total
     * @return List of MemberEntity with totals greater than specified
     */
    @Override
    public List<MemberEntity> getAllMembersAboveATotal(int total) {
        List<MemberEntity> listOfAllEntities = memberRepository.findAll();
        List<MemberEntity> listOfEntitiesToReturn = new ArrayList<>();

        for(MemberEntity memberEntity : listOfAllEntities){
            if(memberEntity.getTotal() > total) listOfEntitiesToReturn.add(memberEntity);
        }
        return listOfEntitiesToReturn;
    }

    /**
     * Registers a new member after checking for email uniqueness.
     *
     * @param memberEntity Member to register
     * @throws IllegalStateException if a member with the same email exists
     */
    @Override
    public void registerNewMember(MemberEntity memberEntity) {
        if(memberRepository.existsByEmail(memberEntity.getEmail())) throw new IllegalStateException("Member with an email of: " + memberEntity.getEmail() + " already exists");
        memberRepository.save(memberEntity);
    }

    /**
     * Registers multiple members at once. All emails must be unique.
     *
     * @param memberEntities List of members to register
     * @throws IllegalStateException if any email already exists
     */
    @Override
    public void registerNewMembers(List<MemberEntity> memberEntities) {
        for(MemberEntity memberEntity : memberEntities){
            if(memberRepository.existsByEmail(memberEntity.getEmail())) throw new IllegalStateException("Member with an email of: " + memberEntity.getEmail() + " already exists");
        }
        memberRepository.saveAll(memberEntities);
    }

    /**
     * Updates the coach assigned to a specific member.
     *
     * @param id       The ID of the member whose coach is being updated.
     * @param coachID  The ID of the coach to assign to the member.
     * @throws IllegalStateException if either the member or the coach does not exist.
     */
    @Override
    public void updateCoachedByById(Long id, Long coachID) {
        MemberEntity memberEntityToUpdate = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Member with an id of: " + id + " doesnt exist"));

        CoachEntity coachEntity = coachRepository.findById(coachID)
                .orElseThrow(() -> new IllegalStateException("Coach with an id of: " + coachID + " doesnt exist"));

        memberEntityToUpdate.setCoachedBy(coachEntity);
        memberRepository.save(memberEntityToUpdate);
    }

    /**
     * Updates a member's name by ID and verifies their email for identity match.
     *
     * @param id    Member ID
     * @param name  New name
     * @param email Email for identity verification
     * @throws IllegalStateException if the ID/email pair is invalid or mismatched
     */
    @Override
    public void updateNameByIdAndEmail(Long id, String name, String email) {
        MemberEntity memberEntityToUpdate = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Member with an id of: " + id + " doesnt exist"));
        if(!memberRepository.existsByEmail(email)) {
            throw new IllegalStateException("Member with an email of: " + email + " doesnt exist");
        } else if (memberEntityToUpdate != memberRepository.findMemberByEmail(email)) {
            throw new IllegalStateException("Member with an id of: " + id + " is not the same member that has an email of: " + email);
        }

        if(name != null && name.length() > 0) {
            memberEntityToUpdate.setName(name);
            memberRepository.save(memberEntityToUpdate);
        } else throw new IllegalStateException("Name provided must be not-null and must not be an empty string");
    }

    /**
     * Updates multiple members' names by their IDs and associated emails.
     *
     * @param ids    List of member IDs
     * @param names  List of names to update
     * @param emails List of emails for identity verification
     * @throws IllegalArgumentException if input sizes mismatch
     */
    @Override
    public void updateMultipleMembersNameByIdAndEmail(List<Long> ids, List<String> names, List<String> emails) {
        if(ids.size() != names.size() || ids.size() != emails.size() || names.size() != emails.size()) {
            throw new IllegalStateException("Size of names, id's, and emails lists must be equal");
        }

        Set<Long> idsCopy = new HashSet<>();
        for(Long id : ids){
            idsCopy.add(id);
            memberRepository.findById(id)
                    .orElseThrow(() -> new IllegalStateException("Member with an id of: " + id + " doesnt exist"));
        }
        Set<String> emailsCopy = new HashSet<>();
        for(String email : emails){
            emailsCopy.add(email);
            if(!memberRepository.existsByEmail(email)) throw new IllegalStateException("Member with an email of: " + email + " doesnt exist");
        }

        if(idsCopy.size() != ids.size()) throw new IllegalStateException("Duplicates detected inside of id list, each id must be unique");
        if(emailsCopy.size() != emails.size()) throw new IllegalStateException("Duplicates detected inside of email list, each email must be unique");

        List<MemberEntity> memberEntitiesToUpdate = memberRepository.findAllById(ids);

        for(int i =0; i < memberEntitiesToUpdate.size(); i++){
            if(names.get(i) == null || names.get(i).isEmpty() || names.get(i).length() < 0) {
                throw new IllegalStateException("Name provided must be not-null and must not be an empty string");
            }
            if (!memberEntitiesToUpdate.get(i).equals(memberRepository.findMemberByEmail(emails.get(i)))) {
                throw new IllegalStateException("Provided ID and email do not belong to the same member");
            }

            memberEntitiesToUpdate.get(i).setName(names.get(i));
        }
        memberRepository.saveAll(memberEntitiesToUpdate);
    }

    /**
     * Updates SBD stats for a member by ID.
     *
     * @param id       Member ID
     * @param bench    New bench value
     * @param squat    New squat value
     * @param deadlift New deadlift value
     * @throws IllegalStateException if member is not found
     */
    @Override
    public void updateSBDStatus(Long id, int bench, int squat, int deadlift) {
        MemberEntity entityToUpdate = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Member with an id of: " + id + " doesnt exist"));

        if(bench < 1 || squat < 1 || deadlift < 1){
            throw new IllegalStateException("Lifts cannot be negative or 0");
        } else {
            int total = bench + squat + deadlift;
            entityToUpdate.setBench(bench);
            entityToUpdate.setSquat(squat);
            entityToUpdate.setDeadlift(deadlift);
            entityToUpdate.setTotal(total);
            memberRepository.save(entityToUpdate);
        }
    }


    /**
     * Fully replaces the data of a member by ID.
     *
     * @param id            Member ID
     * @param memberEntity The full replacement member entity
     * @throws IllegalStateException if no member with the given ID exists
     */
    @Override
    public void updateCompleteMember(Long id, MemberEntity memberEntity) {
        MemberEntity entityToUpdate = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Member with an id of: " + id + " doesnt exist"));
        int total = memberEntity.getBench() + memberEntity.getSquat() + memberEntity.getDeadlift();

        entityToUpdate.setName(memberEntity.getName());
        entityToUpdate.setEmail(memberEntity.getEmail());
        entityToUpdate.setAge(memberEntity.getAge());
        entityToUpdate.setRole(memberEntity.getRole());
        entityToUpdate.setDateOfBirth(memberEntity.getDateOfBirth());
        entityToUpdate.setMembershipDate(memberEntity.getMembershipDate());
        entityToUpdate.setBench(memberEntity.getBench());
        entityToUpdate.setSquat(memberEntity.getSquat());
        entityToUpdate.setDeadlift(memberEntity.getDeadlift());
        entityToUpdate.setTotal(total);

        memberRepository.save(entityToUpdate);
    }

    /**
     * Deletes a member by ID.
     *
     * @param id Member ID
     * @throws IllegalStateException if the member does not exist
     */
    @Override
    public void deleteMemberById(Long id) {
        memberRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Member with an id of: " + id + " doesnt exist"));
        memberRepository.deleteById(id);
    }

    /**
     * Deletes all members whose total is below the specified value.
     *
     * @param total Threshold for deletion
     */
    @Override
    public void deleteMembersBelowATotal(int total) {
        List<MemberEntity> memberEntities = memberRepository.findAll();

        for(MemberEntity memberEntity : memberEntities){
            if(memberEntity.getTotal() < total) memberRepository.deleteById(memberEntity.getId());
        }
    }
}
