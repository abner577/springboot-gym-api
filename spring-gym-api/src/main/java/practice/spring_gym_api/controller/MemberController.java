package practice.spring_gym_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.spring_gym_api.dto.MemberDTO;
import practice.spring_gym_api.dto.MemberMapper;
import practice.spring_gym_api.config.UpdateMultipleMembersRequest;
import practice.spring_gym_api.dto.request.MemberRequestDTO;
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
    @Operation(summary = "Retrieves a member by their ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Member retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    @GetMapping(path = "/members/{member_id}")
    public ResponseEntity<MemberDTO> getMemberById(@PathVariable("member_id") Long id){
        MemberEntity memberEntity = memberService.getMemberById(id);
        MemberDTO memberDTO = memberMapper.convertToMemberDTO(memberEntity);
        return ResponseEntity.ok(memberDTO);
    }

    /**
     * Retrieves all members with pagination support.
     * @param page Page number (default is 0)
     * @param size Number of members per page (default is 5)
     * @return List of paginated MemberDTOs
     */
    @Operation(summary = "Retrieves all members with pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Members retrieved successfully")
    })
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
    @Operation(summary = "Retrieves the member with the highest bench press")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Member retrieved successfully")
    })
    @GetMapping(path = "/members/highest/bench")
    public ResponseEntity<MemberDTO> getMemberWithHighestBench() {
        MemberEntity memberEntity = memberService.getMemberByHighestBench();
        MemberDTO memberDTO = memberMapper.convertToMemberDTO(memberEntity);
        return ResponseEntity.ok(memberDTO);
    }

    /**
     * Gets the member with the highest squat.
     * @return MemberDTO with the highest squat stat
     */
    @Operation(summary = "Retrieves the member with the highest squat")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Member retrieved successfully")
    })
    @GetMapping(path = "/members/highest/squat")
    public MemberDTO getMemberWithHighestSquat() {
        MemberEntity memberEntity = memberService.getMemberByHighestSquat();
        return memberMapper.convertToMemberDTO(memberEntity);
    }

    /**
     * Gets the member with the highest deadlift.
     * @return MemberDTO with the highest deadlift stat
     */
    @Operation(summary = "Retrieves the member with the highest deadlift")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Member retrieved successfully")
    })
    @GetMapping(path = "/members/highest/deadlift")
    public MemberDTO getMemberWithHighestDeadlift() {
        MemberEntity memberEntity = memberService.getMemberByHighestDeadlift();
        return memberMapper.convertToMemberDTO(memberEntity);
    }

    /**
     * Gets the member with the highest combined total (bench + squat + deadlift).
     * @return MemberDTO with the highest total
     */
    @Operation(summary = "Retrieves the member with the highest combined total")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Member retrieved successfully")
    })
    @GetMapping(path = "/members/highest/total")
    public MemberDTO getMemberWithHighestTotal() {
        MemberEntity memberEntity = memberService.getMemberByHighestTotal();
        return memberMapper.convertToMemberDTO(memberEntity);
    }

    /**
     * Gets a list of members whose total exceeds the specified amount.
     * @param total The minimum total to filter by
     * @return List of MemberDTOs
     */
    @Operation(summary = "Retrieves members whose total exceeds a threshold")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Members retrieved successfully")
    })
    @GetMapping(path = "/members/above/total/{member_total}")
    public List<MemberDTO> getAllMembersWithAGreaterTotalThan(@PathVariable("member_total") int total){
        List<MemberEntity> listOfEntities = memberService.getAllMembersAboveATotal(total);
        return listOfEntities
                .stream()
                .map(memberMapper::convertToMemberDTO)
                .toList();
    }

    /**
     * Retrieves all available members who are not currently assigned to a coach.
     *
     * @return a list of {@link MemberDTO} objects representing avaliable members (members without coaches).
     * @throws IllegalStateException if no available members are found (thrown from the service layer).
     */
    @Operation(summary = "Retrieves all members not assigned to any coach")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Members retrieved successfully")
    })
    @GetMapping(path = "/members/no-coach")
    public List<MemberDTO> getAllAvaliableMembers(){
        List<MemberEntity> memberEntities = memberService.getAllAvaliableMembers();
        return memberEntities.stream()
                .map(memberMapper::convertToMemberDTO)
                .toList();
    }

    @Operation(summary = "Registers a single new member")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Member successfully registered"),
            @ApiResponse(responseCode = "400", description = "Invalid member data")
    })
    @PostMapping(path = "/members")
    public void registerOneMember(@Valid @RequestBody MemberRequestDTO memberRequestDTO) {
        memberService.registerNewMember(memberRequestDTO);
    }

    @Operation(summary = "Registers multiple new members at once")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Members successfully registered"),
            @ApiResponse(responseCode = "400", description = "Invalid input or less than two members provided")
    })
    @PostMapping(path = "/members/batch")
    public void registerMultipleMembers(
            @Size(min = 2, message = "At least two members must be provided,  if you only need to register one member use the singular endpoint")
            @NotNull(message = "List of members must not be null")
            @RequestBody List<@Valid MemberRequestDTO> memberRequestDTOS) {
        memberService.registerNewMembers(memberRequestDTOS);
    }

    @Operation(summary = "Replaces the current coach of a member")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Coach successfully replaced"),
            @ApiResponse(responseCode = "400", description = "Invalid member or coach ID")
    })
    @PatchMapping(path = "/members/{member_id}/oldCoach/{oldCoach_ID}/newCoach/{newCoach_id}")
    public void replaceCoach(
            @PathVariable("member_id") Long id,
            @PathVariable("oldCoach_ID") Long oldCoachesID,
            @PathVariable("newCoach_id") Long newCoachesID) {
        memberService.replaceCoach(id, oldCoachesID, newCoachesID);
    }

    @Operation(summary = "Removes the current coach assignment from a member")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Coach successfully removed"),
            @ApiResponse(responseCode = "400", description = "Invalid member ID")
    })
    @PatchMapping(path = "/members/{member_id}/coach/remove")
    public void removeCoachedBy(@PathVariable("member_id") Long id) {
        memberService.removeCoachedBy(id);
    }

    @Operation(summary = "Updates the name of a member using ID and email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Name successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid member ID or email")
    })
    @PatchMapping(path = "/members/{member_id}/name")
    public void updateNameById(
            @PathVariable("member_id") Long id,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "email") String email) {
        memberService.updateNameByIdAndEmail(id, name, email);
    }

    @Operation(summary = "Batch update names of multiple members by IDs and emails")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Names successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PatchMapping(path = "/members/name")
    public void updateListOfNamesByListOfIds(@Valid @RequestBody UpdateMultipleMembersRequest request) {
        memberService.updateMultipleMembersNameByIdAndEmail(request.getIds(), request.getNames(), request.getEmails());
    }

    @Operation(summary = "Updates SBD (squat, bench, deadlift) stats for a member")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SBD stats successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid ID or stat values")
    })
    @PatchMapping(path = "/members/{member_id}/sbd")
    public void updateSBDStats(
            @PathVariable("member_id") Long id,
            @RequestParam String email,
            @RequestParam int bench,
            @RequestParam int squat,
            @RequestParam int deadlift) {
        memberService.updateSBDStatus(id, email, bench, squat, deadlift);
    }

    @Operation(summary = "Updates the role of a member by ID and email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid role or member credentials")
    })
    @PatchMapping(path = "/members/{member_id}/role")
    public void updateRoleOfAMemberByIdAndEmail(
            @PathVariable("member_id") Long id,
            @RequestParam String email,
            @RequestParam String role) {
        memberService.updatedRoleOfAMemberByIdAndEmail(id, email, role);
    }

    @Operation(summary = "Updates a member's full record using ID and email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Member successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid update data or credentials")
    })
    @PutMapping(path = "/members/{member_id}")
    public void updateFullMember(
            @PathVariable("member_id") Long id,
            @RequestParam String email,
            @Valid @RequestBody MemberRequestDTO updatedMemberRequestDTO) {
        memberService.updateCompleteMember(id, email, updatedMemberRequestDTO);
    }

    @Operation(summary = "Deletes a single member by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Member successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid member ID")
    })
    @DeleteMapping(path = "members/{member_id}")
    public void deleteMemberById(@PathVariable("member_id") Long id) {
        memberService.deleteMemberById(id);
    }

    @Operation(summary = "Deletes all members whose total (bench+squat+deadlift) is below a threshold")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Members successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid total value")
    })
    @DeleteMapping(path = "members/below/total/{total}")
    public void deleteAllMembersBelowATotal(@PathVariable("total") int total) {
        memberService.deleteMembersBelowATotal(total);
    }
}
