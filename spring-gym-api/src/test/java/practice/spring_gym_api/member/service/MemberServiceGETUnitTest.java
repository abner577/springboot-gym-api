package practice.spring_gym_api.member.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import practice.spring_gym_api.dto.MemberDTO;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.entity.enums.Roles;
import practice.spring_gym_api.repository.CoachRepository;
import practice.spring_gym_api.repository.MemberRepository;
import practice.spring_gym_api.service.impl.CoachServiceimpl;
import practice.spring_gym_api.service.impl.MemberServiceimpl;
import practice.spring_gym_api.testdata.dto.MemberDTOTestData;
import practice.spring_gym_api.testdata.entity.CoachTestData;
import practice.spring_gym_api.testdata.entity.MemberTestData;
import practice.spring_gym_api.testdata.invalidTestData.InvalidCoachEntity;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceGETUnitTest {

    @InjectMocks
    private MemberServiceimpl memberService;

    @Mock
    private MemberRepository memberRepository;

    private MemberEntity memberEntity1;
    private MemberEntity memberEntity2;
    private MemberDTO memberDTO1;
    private MemberDTO memberDTO2;
    private CoachEntity coachEntity1;

    private String name;
    private String email;

    private String idMessage;
    private String invalidEmail;
    private String emailExistsMessage;
    private String wrongEmail;
    private String noMembers;


    @BeforeEach
    void setup() {
        memberEntity1 = MemberTestData.createSeedMember1();
        memberEntity2 = MemberTestData.createSeedMember2();
        memberDTO1 = MemberDTOTestData.createdSeedMemberDTO1();
        memberDTO2 = MemberDTOTestData.createdSeedMemberDTO2();
        coachEntity1 = CoachTestData.createSeedCoach1();

        name = memberEntity1.getName();
        email = memberEntity1.getEmail();

        idMessage = "Member with an id of: " + 1L + " doesnt exist";
        emailExistsMessage = "Member with an email of: " + memberEntity1.getEmail() + " already exists";
        invalidEmail = "Email cannot be null or empty";
        wrongEmail = "Member with an email of: " + email + " doesnt exist";
        noMembers = "There are currently no members registered";
    }

    @Test
    void getMemberById_SuccessfullyReturnsMemberEntity_WhenIdIsValid() {
        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(memberEntity1));

        MemberEntity member = memberService.getMemberById(1L);
        assertTrue(member.equals(memberEntity1));

        verify(memberRepository, times(1)).findById(1L);
    }

    @Test
    void getMemberById_ThrowsException_WhenIdIsNotValid() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        var exception = assertThrows(NoSuchElementException.class, () -> memberService.getMemberById(1L));
        assertEquals(idMessage, exception.getMessage());

        verify(memberRepository, times(1)).findById(1L);
    }

    @Test
    void getMemberByHighestBench_SuccessfullyReturnsMember_WhenMembersAreAvalaible() {
        when(memberRepository.findAll()).thenReturn(List.of(memberEntity1, memberEntity2));

        MemberEntity memberReturned = memberService.getMemberByHighestBench();
        assertTrue(memberReturned.equals(memberEntity1));

        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void getMemberByHighestBench_ThrowsException_WhenNoMembersAreAvaliable() {
        when(memberRepository.findAll()).thenReturn(new ArrayList<>());

        var exception = assertThrows(NoSuchElementException.class, () -> memberService.getMemberByHighestBench());
        assertEquals(noMembers, exception.getMessage());

        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void getMemberByHighestSquat_SuccessfullyReturnsMember_WhenMembersAreAvalaible() {
        when(memberRepository.findAll()).thenReturn(List.of(memberEntity1, memberEntity2));

        MemberEntity memberReturned = memberService.getMemberByHighestSquat();
        assertTrue(memberReturned.equals(memberEntity1));

        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void getMemberByHighestSquat_ThrowsException_WhenNoMembersAreAvaliable() {
        when(memberRepository.findAll()).thenReturn(new ArrayList<>());

        var exception = assertThrows(NoSuchElementException.class, () -> memberService.getMemberByHighestSquat());
        assertEquals(noMembers, exception.getMessage());

        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void getMemberByHighestDeadlift_SuccessfullyReturnsMember_WhenMembersAreAvalaible() {
        when(memberRepository.findAll()).thenReturn(List.of(memberEntity1, memberEntity2));

        MemberEntity memberReturned = memberService.getMemberByHighestDeadlift();
        assertTrue(memberReturned.equals(memberEntity1));

        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void getMemberByHighestDeadlift_ThrowsException_WhenNoMembersAreAvaliable(){
        when(memberRepository.findAll()).thenReturn(new ArrayList<>());

        var exception = assertThrows(NoSuchElementException.class, () -> memberService.getMemberByHighestDeadlift());
        assertEquals(noMembers, exception.getMessage());

        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void getMemberByHighestTotal_SuccessfullyReturnsMember_WhenMembersAreAvalaible() {
        when(memberRepository.findAll()).thenReturn(List.of(memberEntity1, memberEntity2));

        MemberEntity memberReturned = memberService.getMemberByHighestTotal();
        assertTrue(memberReturned.equals(memberEntity1));

        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void getMemberByHighestTotal_ThrowsException_WhenNoMembersAreAvaliable() {
        when(memberRepository.findAll()).thenReturn(new ArrayList<>());

        var exception = assertThrows(NoSuchElementException.class, () -> memberService.getMemberByHighestTotal());
        assertEquals(noMembers, exception.getMessage());

        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void getAllMembersAboveATotal_SucessfullyReturnsMembers_WhenMembersAreAvaliable() {
        when(memberRepository.findAll()).thenReturn(List.of(memberEntity1, memberEntity2));

        List<MemberEntity> membersReturned = memberService.getAllMembersAboveATotal(200);
        assertThat(membersReturned).hasSize(2);

        for(MemberEntity member : membersReturned) {
            assertTrue(member.getTotal() > 200);
        }
        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void getAllMembersAboveATotal_ThrowsException_WhenMembersArentAvaliable() {
        when(memberRepository.findAll()).thenReturn(new ArrayList<>());

        var exception = assertThrows(NoSuchElementException.class, () -> memberService.getMemberByHighestTotal());
        assertEquals(noMembers, exception.getMessage());

        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void getAllAvaliableMembers_SuccessfullyGetsAllAvaliableMembers_WhenMembersAreAvaliable() {
        when(memberRepository.findAll()).thenReturn(List.of(memberEntity1, memberEntity2));

        List<MemberEntity> membersReturned = memberService.getAllAvaliableMembers();
        assertThat(membersReturned).hasSize(0);

        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void getAllAvaliableMembers_ThrowsException_WhenMembersArentAvaliable() {
        when(memberRepository.findAll()).thenReturn(new ArrayList<>());

        var exception = assertThrows(NoSuchElementException.class, () -> memberService.getAllAvaliableMembers());
        assertEquals(noMembers, exception.getMessage());

        verify(memberRepository, times(1)).findAll();
    }
}
