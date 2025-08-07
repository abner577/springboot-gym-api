package practice.spring_gym_api.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import practice.spring_gym_api.dto.MemberMapper;
import practice.spring_gym_api.dto.request.MemberRequestDTO;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import org.springframework.stereotype.Service;
import practice.spring_gym_api.entity.WorkerEntity;
import practice.spring_gym_api.entity.enums.Roles;
import practice.spring_gym_api.repository.CoachRepository;
import practice.spring_gym_api.repository.MemberRepository;
import practice.spring_gym_api.repository.WorkerRepository;
import practice.spring_gym_api.service.MemberService;

import java.util.*;

/**
 * Service implementation for managing Member-related operations.
 * Handles business logic and interacts with the MemberRepository.
 */
@Service
public class MemberServiceimpl implements MemberService {

    private final MemberRepository memberRepository;
    private final CoachRepository coachRepository;
    private final WorkerRepository workerRepository;
    private final MemberMapper memberMapper;

    public MemberServiceimpl(MemberRepository memberRepository, CoachRepository coachRepository, WorkerRepository workerRepository, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.coachRepository = coachRepository;
        this.workerRepository = workerRepository;
        this.memberMapper = memberMapper;
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
                .orElseThrow(() -> new NoSuchElementException("Member with an id of: " + id + " doesnt exist"));
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
        if(listOfAllEntities.isEmpty()) throw new NoSuchElementException("There are currently no members registered");

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
        if(listOfAllEntities.isEmpty()) throw new NoSuchElementException("There are currently no members registered");

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
        if(listOfAllEntities.isEmpty()) throw new NoSuchElementException("There are currently no members registered");

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
        if(listOfAllEntities.isEmpty()) throw new NoSuchElementException("There are currently no members registered");

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
        if(listOfAllEntities.isEmpty()) throw new NoSuchElementException("There are currently no members registered");
        List<MemberEntity> listOfEntitiesToReturn = new ArrayList<>();

        for(MemberEntity memberEntity : listOfAllEntities){
            if(memberEntity.getTotal() > total) listOfEntitiesToReturn.add(memberEntity);
        }
        return listOfEntitiesToReturn;
    }

    /**
     * Finds and returns all members who are not currently coached by anyone.
     *
     * This method filters the entire list of members and includes only those
     * whose {@code coachedBy} property is {@code null}.
     *
     * @return a list of {@link MemberEntity} objects who are available (not assigned to any coach).
     */
    @Override
    public List<MemberEntity> getAllAvaliableMembers() {
        List<MemberEntity> memberEntities = memberRepository.findAll();
        if(memberEntities.isEmpty()) throw new NoSuchElementException("There are currently no members registered");
        List<MemberEntity> memberEntitiesToReturn = new ArrayList<>();

        for(MemberEntity memberEntity : memberEntities){
            if(memberEntity.getCoachedBy() == null) memberEntitiesToReturn.add(memberEntity);
        }
        return memberEntitiesToReturn;
    }

    /**
     * Registers a new member after checking for email uniqueness.
     *
     * @param memberRequestDTO Member to register
     * @throws IllegalStateException if a member with the same email exists
     */
    @Override
    public void registerNewMember(MemberRequestDTO memberRequestDTO) {
        if(memberRepository.existsByEmail(memberRequestDTO.getEmail())) throw new IllegalStateException("Member with an email of: " + memberRequestDTO.getEmail() + " already exists");
        if(!Objects.equals(memberRequestDTO.getRole(), "ROLE_MEMBER")) throw new IllegalArgumentException("Role must be 'ROLE_MEMBER'");
        MemberEntity memberEntity = memberMapper.convertToMemberEntity(memberRequestDTO);
        memberRepository.save(memberEntity);
    }

    /**
     * Registers multiple members at once. All emails must be unique.
     *
     * @param memberRequestDTOS List of members to register
     * @throws IllegalStateException if any email already exists
     */
    @Override
    public void registerNewMembers(List<MemberRequestDTO> memberRequestDTOS) {
        List<MemberEntity> memberEntities = new ArrayList<>();
        for(MemberRequestDTO memberRequestDTO : memberRequestDTOS){
            if(memberRepository.existsByEmail(memberRequestDTO.getEmail())) throw new IllegalStateException("Member with an email of: " + memberRequestDTO.getEmail() + " already exists");
            if(!Objects.equals(memberRequestDTO.getRole(), "ROLE_COACH")) throw new IllegalArgumentException("Role must be 'ROLE_MEMBER'");
            memberEntities.add(memberMapper.convertToMemberEntity(memberRequestDTO));
        }
        memberRepository.saveAll(memberEntities);
    }

    /**
     * Replaces the coach assigned to a specific member.
     *
     * @param id             The ID of the member whose coach is being updated.
     * @param oldCoachesID   The ID of the current coach assigned to the member.
     * @param newCoachesID   The ID of the new coach to assign to the member.
     * @throws IllegalStateException if the member, old coach, or new coach does not exist.
     */
    @Override
    public void replaceCoach(Long id, Long oldCoachesID, Long newCoachesID) {
        MemberEntity memberEntityToUpdate = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Member with an id of: " + id + " doesnt exist"));

        CoachEntity oldCoachEntity = coachRepository.findById(oldCoachesID)
                .orElseThrow(() -> new NoSuchElementException("Coach with an id of: " + oldCoachesID + " doesnt exist"));

        CoachEntity newCoachEntity = coachRepository.findById(newCoachesID)
                .orElseThrow(() -> new NoSuchElementException("Coach with an id of: " + newCoachesID + " doesnt exist"));

        memberEntityToUpdate.setCoachedBy(newCoachEntity);
        memberRepository.save(memberEntityToUpdate);
    }

    /**
     * Removes the coach assigned to a specific member.
     *
     * @param id Member ID
     * @throws IllegalStateException if member is not found
     */
    @Override
    public void removeCoachedBy(Long id) {
        MemberEntity memberEntityToUpdate = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Member with an id of: " + id + " doesnt exist"));
        memberEntityToUpdate.setCoachedBy(null);
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
        if(name.isEmpty() || name == null) throw new IllegalArgumentException("Name cannot be null or empty");
        if(email.isEmpty() || email == null) throw new IllegalArgumentException("Email cannot be null or empty");

        MemberEntity memberEntityToUpdateById = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Member with an id of: " + id + " doesnt exist"));

        MemberEntity memberEntityToUpdateByEmail = memberRepository.findMemberByEmail(email);
        if(memberEntityToUpdateByEmail == null)  throw new NoSuchElementException("Member with an email of: " + email + " doesnt exist");
        if(!memberEntityToUpdateById.equals(memberEntityToUpdateByEmail)) throw new IllegalStateException("Member with an id of: " + id + " is not the same member that has an email of: " + email);

            memberEntityToUpdateById.setName(name);
            memberRepository.save(memberEntityToUpdateById);
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
                    .orElseThrow(() -> new NoSuchElementException("Member with an id of: " + id + " doesnt exist"));
        }
        Set<String> emailsCopy = new HashSet<>();
        for(String email : emails){
            emailsCopy.add(email);
            if(!memberRepository.existsByEmail(email)) throw new NoSuchElementException("Member with an email of: " + email + " doesnt exist");
        }

        if(idsCopy.size() != ids.size()) throw new IllegalStateException("Duplicates detected inside of id list, each id must be unique");
        if(emailsCopy.size() != emails.size()) throw new IllegalStateException("Duplicates detected inside of email list, each email must be unique");

        List<MemberEntity> memberEntitiesToUpdate = memberRepository.findAllById(ids);

        for(int i =0; i < memberEntitiesToUpdate.size(); i++){
            if(names.get(i) == null || names.get(i).isEmpty() || names.get(i).length() == 0) {
                throw new IllegalArgumentException("Name provided must be not-null and must not be an empty string");
            }
            if (!memberEntitiesToUpdate.get(i).equals(memberRepository.findMemberByEmail(emails.get(i)))) {
                throw new IllegalArgumentException("Provided ID and email do not belong to the same member");
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
    public void updateSBDStatus(Long id, String email, int bench, int squat, int deadlift) {
        if(email.isEmpty() || email == null) throw new IllegalArgumentException("Email cannot be null or empty");

        MemberEntity entityToUpdateID = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Member with an id of: " + id + " doesnt exist"));

        MemberEntity entityToUpdateEmail = memberRepository.findMemberByEmail(email);
        if(entityToUpdateEmail == null) throw new NoSuchElementException("Member with an email of: " + email + " doesnt exist");
        if(!entityToUpdateID.equals(entityToUpdateEmail)) throw new IllegalStateException("Member with an id of: " + id + " is not the same member that has an email of: " + email);

        if(bench < 0 || squat < 0 || deadlift < 0){
            throw new IllegalArgumentException("Lifts cannot be negative");
        } else {
            int total = bench + squat + deadlift;
            entityToUpdateID.setBench(bench);
            entityToUpdateID.setSquat(squat);
            entityToUpdateID.setDeadlift(deadlift);
            entityToUpdateID.setTotal(total);
            memberRepository.save(entityToUpdateID);
        }
    }

    /**
     * Fully replaces the data of a member by ID.
     *
     * @param id            Member ID
     * @param memberRequestDTO The full replacement member entity
     * @throws IllegalStateException if no member with the given ID exists
     */
    @Override
    public void updateCompleteMember(Long id, String email, MemberRequestDTO memberRequestDTO) {
        MemberEntity entityToUpdate = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Member with an id of: " + id + " doesnt exist"));

        if(email.isEmpty() || email == null) throw new IllegalArgumentException("Email cannot be null or empty");

        MemberEntity entityToUpdateEmail = memberRepository.findMemberByEmail(email);
        if(entityToUpdateEmail == null) throw new NoSuchElementException("Member with an email of: " + email + " doesnt exist");
        if(!entityToUpdate.equals(entityToUpdateEmail)) throw new IllegalStateException("Member with an id of: " + id + " is not the same member that has an email of: " + email);

        if(!Objects.equals(memberRequestDTO.getRole(), "ROLE_MEMBER")) throw new IllegalStateException("role must be 'ROLE_MEMBER'");

        // if trying to update email
        if(!Objects.equals(entityToUpdateEmail.getEmail(), memberRequestDTO.getEmail())) {
            MemberEntity memberEntity1 = memberRepository.findMemberByEmail(memberRequestDTO.getEmail());
            if(memberEntity1 != null) throw new IllegalStateException("The updated email that you are trying to give to " + memberRequestDTO.getName() + " is already registered under another member");
        }

        int total = memberRequestDTO.getBench() + memberRequestDTO.getSquat() + memberRequestDTO.getDeadlift();

        entityToUpdate.setName(memberRequestDTO.getName());
        entityToUpdate.setEmail(memberRequestDTO.getEmail());
        entityToUpdate.setDateOfBirth(memberRequestDTO.getDateOfBirth());
        entityToUpdate.setMembershipDate(memberRequestDTO.getMembershipDate());
        entityToUpdate.setBench(memberRequestDTO.getBench());
        entityToUpdate.setSquat(memberRequestDTO.getSquat());
        entityToUpdate.setDeadlift(memberRequestDTO.getDeadlift());
        entityToUpdate.setTotal(total);

        memberRepository.save(entityToUpdate);
    }

    /**
     * Updates the role of a member
     *
     * @param id            Member ID
     * @param role The new role assigned to the member
     * @throws IllegalStateException if no member with the given ID exists
     * @throws IllegalStateException if user tries to assign member a role that they already have
     */
    @Override
    public void updatedRoleOfAMemberByIdAndEmail(Long id, String email, String role) {
        MemberEntity memberEntityById = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Member with an id of: " + id + " doesnt exist"));

        if(email.isEmpty() || email == null) throw new IllegalArgumentException("Email cannot be null or empty");
        MemberEntity memberEntityByEmail = memberRepository.findMemberByEmail(email);

        // Validation of credentials
        if(memberEntityByEmail == null) throw new NoSuchElementException("Member with an email of: " + email + " doesnt exist");
        if(!memberEntityById.equals(memberEntityByEmail)) throw new IllegalStateException("Member with an id of: " + id + " is not the same member that has an email of: " + email);

        // Checking which role to update member to
        if(role.equalsIgnoreCase(String.valueOf(Roles.ROLE_MEMBER))) throw new IllegalStateException("Member: " + memberEntityById.getName() + " already has a role of ROLE_MEMBER");
        else if (role.equalsIgnoreCase(String.valueOf(Roles.ROLE_COACH))) {
            CoachEntity coachEntityFromMember = memberMapper.convertMemberToCoachEntity(memberEntityById);

            deleteMemberById(id);
            coachRepository.save(coachEntityFromMember);
        }
        else if (role.equalsIgnoreCase(String.valueOf(Roles.ROLE_WORKER))) {
            WorkerEntity workerEntityFromMember = memberMapper.convertMemberToWorkerEntity(memberEntityById);

            deleteMemberById(id);
            workerRepository.save(workerEntityFromMember);
        }
        else throw new IllegalArgumentException("Role must be either ROLE_COACH, ROLE_WORKER, or ROLE_MEMBER");
    }

    /**
     * Deletes a member by ID.
     *
     * @param id Member ID
     * @throws IllegalStateException if the member does not exist
     */
    @Override
    public void deleteMemberById(Long id) {
       MemberEntity memberEntity = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Member with an id of: " + id + " doesnt exist"));

       memberEntity.setCoachedBy(null);

       memberRepository.save(memberEntity);
       memberRepository.deleteById(id);
    }

    /**
     * Deletes all members whose total is below the specified value.
     *
     * @param total Threshold for deletion
     */
    @Override
    public void deleteMembersBelowATotal(int total) {
        if(total < 0) throw new IllegalArgumentException("Total cannot be negative");

        List<MemberEntity> memberEntities = memberRepository.findAll();
        List<MemberEntity> memberEntitiesToDelete = new ArrayList<>();

        for(MemberEntity memberEntity : memberEntities){
            if(memberEntity.getTotal() <= total) {
                memberEntitiesToDelete.add(memberEntity);
                memberEntity.setCoachedBy(null);
            }
        }

        memberRepository.saveAll(memberEntitiesToDelete);
        memberRepository.deleteAll(memberEntitiesToDelete);
    }
}
