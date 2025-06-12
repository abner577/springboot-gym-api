package practice.spring_gym_api.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import practice.spring_gym_api.dto.MemberDTO;
import practice.spring_gym_api.dto.MemberMapper;
import practice.spring_gym_api.dto.UpdateMultipleMembersRequest;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.service.MemberService;

import java.util.List;

/**
 * Controller class for handling all HTTP requests related to gym members.
 * Provides endpoints for retrieving, registering, updating, and deleting members.
 */
@RestController
@RequestMapping(path = "api/v1/gym-api")
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper memberMapper;

    public MemberController(MemberService memberService, @Qualifier("memberMapperimpl")MemberMapper memberMapper) {
        this.memberService = memberService;
        this.memberMapper= memberMapper;
    }

    /**
     * Retrieves a member by their ID.
     * @param id ID of the member
     * @return MemberDTO representation of the member
     */
    @GetMapping(path = "/member/{member_id}")
    public MemberDTO getMemberById(@PathVariable("member_id") Long id){
        MemberEntity memberEntity = memberService.getMemberById(id);
        MemberDTO memberDTO = memberMapper.convertToMemberDTO(memberEntity);
        return memberDTO;
    }

    /**
     * Retrieves all members with pagination support.
     * @param page Page number (default is 0)
     * @param size Number of members per page (default is 5)
     * @return List of paginated MemberDTOs
     */
    @GetMapping(path = "/members")
    public List<MemberDTO> getAllMember(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<MemberEntity> memberEntities = memberService.getAllMembers(pageable);

        return memberEntities
                .stream()
                .map(memberMapper::convertToMemberDTO)
                .toList();
    }

    /**
     * Gets the member with the highest bench press.
     * @return MemberDTO with the highest bench stat
     */
    @GetMapping(path = "/member/highest/bench")
    public MemberDTO getMemberWithHighestBench() {
        MemberEntity memberEntity = memberService.getMemberByHighestBench();
        MemberDTO memberDTO = memberMapper.convertToMemberDTO(memberEntity);
        return memberDTO;
    }


    /**
     * Gets the member with the highest squat.
     * @return MemberDTO with the highest squat stat
     */
    @GetMapping(path = "/member/highest/squat")
    public MemberDTO getMemberWithHighestSquat() {
        MemberEntity memberEntity = memberService.getMemberByHighestSquat();
        return memberMapper.convertToMemberDTO(memberEntity);
    }

    /**
     * Gets the member with the highest deadlift.
     * @return MemberDTO with the highest deadlift stat
     */
    @GetMapping(path = "/member/highest/deadlift")
    public MemberDTO getMemberWithHighestDeadlift() {
        MemberEntity memberEntity = memberService.getMemberByHighestDeadlift();
        return memberMapper.convertToMemberDTO(memberEntity);
    }

    /**
     * Gets the member with the highest combined total (bench + squat + deadlift).
     * @return MemberDTO with the highest total
     */
    @GetMapping(path = "/member/highest/total")
    public MemberDTO getMemberWithHighestTotal() {
        MemberEntity memberEntity = memberService.getMemberByHighestTotal();
        return memberMapper.convertToMemberDTO(memberEntity);
    }

    /**
     * Gets a list of members whose total exceeds the specified amount.
     * @param total The minimum total to filter by
     * @return List of MemberDTOs
     */
    @GetMapping(path = "/members/above/total/{member_total}")
    public List<MemberDTO> getAllMembersWithAGreaterTotalThan(@PathVariable("member_total") int total){
        List<MemberEntity> listOfEntities = memberService.getAllMembersAboveATotal(total);
        return listOfEntities
                .stream()
                .map(memberMapper::convertToMemberDTO)
                .toList();
    }

    /**
     * Registers a single new member.
     * @param memberEntity The member entity to be saved
     */
    @PostMapping(path = "/member")
    public void registerOneMember(@Valid @RequestBody MemberEntity memberEntity){
        memberService.registerNewMember(memberEntity);
    }

    /**
     * Registers multiple new members at once.
     * Validates that at least 2 members are submitted.
     * @param memberEntities List of new member entities
     */
    @PostMapping(path = "/members")
    public void registerMultipleMembers(
            @Size(min = 2, message = "At least two members must be provided,  if you only need to register one member use the singular endpoint")
            @NotNull(message = "List of members must not be null")
            @RequestBody List<@Valid MemberEntity> memberEntities){
        memberService.registerNewMembers(memberEntities);
    }

    /**
     * Updates the name of a member using their ID and email for validation.
     * @param id Member ID
     * @param name New name
     * @param email Current email
     */
    @PatchMapping(path = "/member/{member_id}")
    public void updateNameById(
            @PathVariable("member_id") Long id,
           @RequestParam(name = "name", required = true) String name,
           @RequestParam(name = "email", required = true) String email
    ) {
        memberService.updateNameByIdAndEmail(id, name, email);
    }

    /**
     * Batch update names of multiple members based on lists of IDs and emails.
     * @param request Object containing IDs, names, and emails
     */
    @PatchMapping(path = "/update/members")
    public void updateListOfNamesByListOfIds(@Valid @RequestBody UpdateMultipleMembersRequest request){
        memberService.updateMultipleMembersNameByIdAndEmail(request.getIds(), request.getNames(), request.getEmails());
    }

    /**
     * Updates SBD (squat, bench, deadlift) stats for a member.
     * @param id Member ID
     * @param bench New bench value
     * @param squat New squat value
     * @param deadlift New deadlift value
     */
    @PatchMapping(path = "/update/sbd/{member_id}")
    public void updateSBDStats(
            @PathVariable("member_id") Long id,
            @RequestParam(required = true) int bench,
            @RequestParam(required = true) int squat,
            @RequestParam(required = true) int deadlift
    ) {
        memberService.updateSBDStatus(id, bench, squat, deadlift);
    }

    /**
     * Updates a member's entire record using their ID.
     * @param id Member ID
     * @param updatedEntity Full MemberEntity with updated fields
     */
    @PutMapping(path = "/member/{member_id}")
    public void updateFullMember(
            @PathVariable("member_id") Long id,
            @Valid @RequestBody MemberEntity updatedEntity
    ){
        memberService.updateCompleteMember(id, updatedEntity);
    }

    /**
     * Deletes a single member by ID.
     * @param id Member ID
     */
    @DeleteMapping(path = "member/{member_id}")
    public void deleteMemberById(@PathVariable("member_id") Long id){
        memberService.deleteMemberById(id);
    }

    /**
     * Deletes all members whose total (bench + squat + deadlift) is below the specified value.
     * @param total The minimum total threshold
     */
    @DeleteMapping(path = "delete/all/below/total")
    public void deleteAllMembersBelowATotal(@RequestParam(required = true) int total){
        memberService.deleteMembersBelowATotal(total);
    }
}
