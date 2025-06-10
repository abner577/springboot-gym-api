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
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.service.MemberService;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/gym-api")
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper memberMapper;

    public MemberController(MemberService memberService, @Qualifier("memberMapperimpl")MemberMapper memberMapper) {
        this.memberService = memberService;
        this.memberMapper= memberMapper;
    }

    @GetMapping(path = "/member/{member_id}")
    public MemberDTO getMemberById(@PathVariable("member_id") Long id){
        MemberEntity memberEntity = memberService.getMemberById(id);
        MemberDTO memberDTO = memberMapper.convertToMemberDTO(memberEntity);
        return memberDTO;
    }

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

    @GetMapping(path = "/member/highest/bench")
    public MemberDTO getMemberWithHighestBench() {
        MemberEntity memberEntity = memberService.getMemberByHighestBench();
        MemberDTO memberDTO = memberMapper.convertToMemberDTO(memberEntity);
        return memberDTO;
    }

    @GetMapping(path = "/member/highest/squat")
    public MemberDTO getMemberWithHighestSquat() {
        MemberEntity memberEntity = memberService.getMemberByHighestSquat();
        return memberMapper.convertToMemberDTO(memberEntity);
    }

    @GetMapping(path = "/member/highest/deadlift")
    public MemberDTO getMemberWithHighestDeadlift() {
        MemberEntity memberEntity = memberService.getMemberByHighestDeadlift();
        return memberMapper.convertToMemberDTO(memberEntity);
    }

    @GetMapping(path = "/member/highest/total")
    public MemberDTO getMemberWithHighestTotal() {
        MemberEntity memberEntity = memberService.getMemberByHighestTotal();
        return memberMapper.convertToMemberDTO(memberEntity);
    }

    @GetMapping(path = "/members/above/total/{member_total}")
    public List<MemberDTO> getAllMembersWithAGreaterTotalThan(@PathVariable("member_total") int total){
        List<MemberEntity> listOfEntities = memberService.getAllMembersAboveATotal(total);
        return listOfEntities
                .stream()
                .map(memberMapper::convertToMemberDTO)
                .toList();
    }

    @PostMapping(path = "/member")
    public void registerOneMember(@Valid @RequestBody MemberEntity memberEntity){
        memberService.registerNewMember(memberEntity);
    }

    @PostMapping(path = "/members")
    public void registerMultipleMembers(
            @Size(min = 2, message = "At least two members must be provided,  if you only need to register one member use the singular endpoint")
            @NotNull(message = "List of members must not be null")
            @RequestBody List<@Valid MemberEntity> memberEntities){
        memberService.registerNewMembers(memberEntities);
    }

    @PatchMapping(path = "/member/{member_id}")
    public void updateNameById(
            @PathVariable("member_id") Long id,
           @RequestParam(name = "name", required = true) String name,
           @RequestParam(name = "email", required = true) String email
    ) {
        memberService.updateNameByIdAndEmail(id, name, email);
    }

    @PatchMapping(path = "/update/members")
    public void updateListOfNamesByListOfIds(@Valid @RequestBody UpdateMultipleMembersRequest request){
        memberService.updateMultipleMembersNameByIdAndEmail(request.getIds(), request.getNames(), request.getEmails());
    }

    @PatchMapping(path = "/update/sbd/{member_id}")
    public void updateSBDStats(
            @PathVariable("member_id") Long id,
            @RequestParam(required = true) int bench,
            @RequestParam(required = true) int squat,
            @RequestParam(required = true) int deadlift
    ) {
        memberService.updateSBDStatus(id, bench, squat, deadlift);
    }

    @PutMapping(path = "/member/{member_id}")
    public void updateFullMember(
            @PathVariable("member_id") Long id,
            @Valid @RequestBody MemberEntity updatedEntity
    ){
        memberService.updateCompleteMember(id, updatedEntity);
    }

    @DeleteMapping(path = "member/{member_id}")
    public void deleteMemberById(@PathVariable("member_id") Long id){
        memberService.deleteMemberById(id);
    }

    @DeleteMapping(path = "delete/all/below/total")
    public void deleteAllMembersBelowATotal(@RequestParam(required = true) int total){
        memberService.deleteMembersBelowATotal(total);
    }
}
