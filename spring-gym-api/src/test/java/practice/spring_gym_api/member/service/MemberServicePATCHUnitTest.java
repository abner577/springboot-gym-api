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
public class MemberServicePATCHUnitTest {

    @InjectMocks
    private MemberServiceimpl memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CoachRepository coachRepository;

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
    private String differentMembers;


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
        differentMembers = "Member with an id of: " + 1L + " is not the same member that has an email of: " + email;
    }

    @Test
    void replaceCoach_SuccessfullyReplacesCoach_WhenCredentialsAreValid() {
        CoachEntity coachEntity2 = CoachTestData.createSeedCoach2();

        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(memberEntity1));
        when(coachRepository.findById(2L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findById(3L)).thenReturn(Optional.ofNullable(coachEntity2));

        memberService.replaceCoach(1L, 2L, 3L);

        assertTrue(!memberEntity1.getCoachedBy().equals(coachEntity1));
        assertThat(!coachEntity1.getClients().contains(memberEntity1));

        assertTrue(memberEntity1.getCoachedBy().equals(coachEntity2));
        assertThat(coachEntity2.getClients().contains(memberEntity1));

        verify(memberRepository, times(1)).findById(1L);
        verify(coachRepository, times(1)).findById(2L);
        verify(coachRepository, times(1)).findById(3L);
        verify(memberRepository, times(1)).save(memberEntity1);
    }

    @Test
    void replaceCoach_ThrowsException_WhenMemberIdIsInvalid() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        var exception = assertThrows(NoSuchElementException.class, () -> memberService.replaceCoach(1L, 2L ,3L));
        assertEquals(idMessage, exception.getMessage());

        verify(memberRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(memberRepository);
        verifyNoInteractions(coachRepository);
    }

    @Test
    void replaceCoach_ThrowsException_WhenCoachIdIsInvalid() {
        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(memberEntity1));
        when(coachRepository.findById(2L)).thenReturn(Optional.empty());

        var exception = assertThrows(NoSuchElementException.class, () -> memberService.replaceCoach(1L, 2L ,3L));
        assertEquals("Coach with an id of: " + 2L + " doesnt exist", exception.getMessage());

        verify(memberRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(memberRepository);
        verify(coachRepository, times(1)).findById(2L);
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void removeCoachedBy_SuccessfullyRemovesCoachedBy_WhenIdIsValid() {
        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(memberEntity1));

        memberService.removeCoachedBy(1L);
        assertThat(memberEntity1.getCoachedBy() == null);

        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).save(memberEntity1);
    }

    @Test
    void removeCoachedBy_ThrowsException_WhenIdIsntValid() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        var exception = assertThrows(NoSuchElementException.class, () -> memberService.removeCoachedBy(1L));
        assertEquals(idMessage, exception.getMessage());

        verify(memberRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    void updateNameByIdAndEmail_SuccessfullyUpdatesName_WhenCredentialsAreValid() {
        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(memberEntity1));
        when(memberRepository.findMemberByEmail(email)).thenReturn(memberEntity1);

        memberService.updateNameByIdAndEmail(1L, "example", email);

        assertThat(memberEntity1.getName().equals("example"));

        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).findMemberByEmail(email);
        verify(memberRepository, times(1)).save(memberEntity1);
    }

    @Test
    void updateNameByIdAndEmail_ThrowsException_WhenNameIsntValid() {
        var exception = assertThrows(IllegalArgumentException.class, () ->
                memberService.updateNameByIdAndEmail(1L, "", "example@gmail.com"));
        assertEquals("Name cannot be null or empty", exception.getMessage());

        verifyNoInteractions(memberRepository);
    }

    @Test
    void updateNameByIdAndEmail_ThrowsException_WhenEmailIsntValid() {
        var exception = assertThrows(IllegalArgumentException.class, () ->
                memberService.updateNameByIdAndEmail(1L, "example", ""));
        assertEquals(invalidEmail, exception.getMessage());

        verifyNoInteractions(memberRepository);
    }

    @Test
    void updateNameByIdAndEmail_ThrowsException_WhenIdDoesntExist() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        var exception = assertThrows(NoSuchElementException.class, () ->
                memberService.updateNameByIdAndEmail(1L, name, email));
        assertEquals(idMessage, exception.getMessage());

        verify(memberRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    void updateNameByIdAndEmail_ThrowsException_WhenEmailDoesntExist() {
        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(memberEntity1));
        when(memberRepository.findMemberByEmail(email)).thenReturn(null);

        var exception = assertThrows(NoSuchElementException.class, () ->
                memberService.updateNameByIdAndEmail(1L, name, email));
        assertEquals(wrongEmail, exception.getMessage());

        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).findMemberByEmail(email);
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    void updateNameByIdAndEmail_ThrowsException_WhenIdAndEmailDontBelongToSameMember() {
        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(memberEntity1));
        when(memberRepository.findMemberByEmail(email)).thenReturn(memberEntity2);

        var exception = assertThrows( IllegalStateException.class, () ->
                memberService.updateNameByIdAndEmail(1L, name, email));
        assertEquals(differentMembers, exception.getMessage());

        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).findMemberByEmail(email);
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    void updateMultipleMembersNameByIdAndEmail_ThrowsException_WhenSizeOfListsArentEqual() {

    }

    @Test
    void updateMultipleMembersNameByIdAndEmail_ThrowsException_WhenIdIsInvalid() {

    }

    @Test
    void updateMultipleMembersNameByIdAndEmail_ThrowsException_WhenEmailIsInvalid() {

    }

    @Test
    void updateMultipleMembersNameByIdAndEmail_ThrowsException_WhenDuplicateIdsInIdList() {

    }

    @Test
    void updateMultipleMembersNameByIdAndEmail_ThrowsException_WhenDuplicateEmailsInEmailList() {

    }

    @Test
    void updateMultipleMembersNameByIdAndEmail_ThrowsException_WhenNameIsInvalid() {

    }

    @Test
    void updateMultipleMembersNameByIdAndEmail_SuccessfullyUpdatesNames_WhenIdAndEmailDontBelongToSameMember() {

    }

}
