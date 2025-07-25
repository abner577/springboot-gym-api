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
import practice.spring_gym_api.repository.CoachRepository;
import practice.spring_gym_api.repository.MemberRepository;
import practice.spring_gym_api.service.impl.MemberServiceimpl;
import practice.spring_gym_api.testdata.dto.MemberDTOTestData;
import practice.spring_gym_api.testdata.entity.CoachTestData;
import practice.spring_gym_api.testdata.entity.MemberTestData;

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
    private String emailAlreadyExistsMessage;
    private String emailDoesntExistMessage;
    private String differentMembers;

    private List<Long> ids;
    private List<String> names;
    private List<String> emails;


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
        emailAlreadyExistsMessage = "Member with an email of: " + memberEntity1.getEmail() + " already exists";
        invalidEmail = "Email cannot be null or empty";
        emailDoesntExistMessage = "Member with an email of: " + email + " doesnt exist";
        differentMembers = "Member with an id of: " + 1L + " is not the same member that has an email of: " + email;

        ids = new ArrayList<>();
        ids.add(1L);
        names = new ArrayList<>();
        names.add("a");
        emails = new ArrayList<>();
        emails.add("@.com");
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
        assertEquals(emailDoesntExistMessage, exception.getMessage());

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
        names.add("b");
        var exception = assertThrows(IllegalStateException.class, () ->
                memberService.updateMultipleMembersNameByIdAndEmail(ids, names, emails));
        assertEquals("Size of names, id's, and emails lists must be equal", exception.getMessage());

        verifyNoInteractions(memberRepository);
    }

    @Test
    void updateMultipleMembersNameByIdAndEmail_ThrowsException_WhenIdIsInvalid() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        var exception = assertThrows(NoSuchElementException.class, () ->
                memberService.updateMultipleMembersNameByIdAndEmail(ids, names, emails));
        assertEquals(idMessage, exception.getMessage());

        verify(memberRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    void updateMultipleMembersNameByIdAndEmail_ThrowsException_WhenEmailIsInvalid() {
        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(memberEntity1));
        when(memberRepository.existsByEmail(email)).thenReturn(false);

        var exception = assertThrows(NoSuchElementException.class, () ->
                memberService.updateMultipleMembersNameByIdAndEmail(ids, names, List.of(email)));
        assertEquals(emailDoesntExistMessage, exception.getMessage());

        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).existsByEmail(email);
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    void updateMultipleMembersNameByIdAndEmail_ThrowsException_WhenDuplicateIdsInIdList() {
        ids.add(1L);
        emails.add("123@.com");
        names.add("bob");

        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(memberEntity1));
        when(memberRepository.existsByEmail(emails.get(0))).thenReturn(true);
        when(memberRepository.existsByEmail(emails.get(1))).thenReturn(true);

        var exception = assertThrows(IllegalStateException.class, () ->
                memberService.updateMultipleMembersNameByIdAndEmail(ids, names, emails));
        assertEquals("Duplicates detected inside of id list, each id must be unique",
                exception.getMessage());

        verify(memberRepository, times(2)).findById(1L);
        verify(memberRepository, times(1)).existsByEmail(emails.get(0));
        verify(memberRepository, times(1)).existsByEmail(emails.get(1));
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    void updateMultipleMembersNameByIdAndEmail_ThrowsException_WhenDuplicateEmailsInEmailList() {
        ids.add(2L);
        emails.add("@.com");
        names.add("bob");

        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(memberEntity1));
        when(memberRepository.findById(2L)).thenReturn(Optional.ofNullable(memberEntity2));
        when(memberRepository.existsByEmail(emails.get(0))).thenReturn(true);
        when(memberRepository.existsByEmail(emails.get(1))).thenReturn(true);

        var exception = assertThrows(IllegalStateException.class, () ->
                memberService.updateMultipleMembersNameByIdAndEmail(ids, names, emails));
        assertEquals("Duplicates detected inside of email list, each email must be unique",
                exception.getMessage());

        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).findById(2L);
        verify(memberRepository, times(2)).existsByEmail(emails.get(0));
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    void updateMultipleMembersNameByIdAndEmail_ThrowsException_WhenNameIsInvalid() {
        ids.add(2L);
        emails.add("@123.com");
        names.removeFirst();
        names.add("");
        names.add("");

        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(memberEntity1));
        when(memberRepository.findById(2L)).thenReturn(Optional.ofNullable(memberEntity2));
        when(memberRepository.existsByEmail(emails.get(0))).thenReturn(true);
        when(memberRepository.existsByEmail(emails.get(1))).thenReturn(true);
        when(memberRepository.findAllById(ids)).thenReturn(List.of(memberEntity1, memberEntity2));

        System.out.println(names);
        var exception = assertThrows(IllegalArgumentException.class, () ->
                memberService.updateMultipleMembersNameByIdAndEmail(ids, names, emails));
        assertEquals("Name provided must be not-null and must not be an empty string",
                exception.getMessage());

        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).findById(2L);
        verify(memberRepository, times(1)).existsByEmail(emails.get(0));
        verify(memberRepository, times(1)).existsByEmail(emails.get(1));
        verify(memberRepository, times(1)).findAllById(ids);
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    void updateSBDStatus_SuccessfullyUpdatesStats_WhenCredentialsAreValid() {
        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(memberEntity1));
        when(memberRepository.findMemberByEmail(email)).thenReturn(memberEntity1);

        memberService.updateSBDStatus(1L, email, 325, 425, 550);
        assertThat(memberEntity1.getBench() == 325 && memberEntity1.getSquat() == 425
        && memberEntity1.getDeadlift() == 550 && memberEntity1.getTotal() == 325 + 425 + 550);

        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).findMemberByEmail(email);
        verify(memberRepository, times(1)).save(memberEntity1);
    }

    @Test
    void updateSBDStatus_ThrowsException_WhenEmailIsInvalid() {
        var exception = assertThrows(IllegalArgumentException.class, () -> memberService.updateSBDStatus(
                1L, "", 1,1,1
        ));
        assertEquals(invalidEmail, exception.getMessage());
        verifyNoInteractions(memberRepository);
    }

    @Test
    void updateSBDStatus_ThrowsException_WhenIdIsInvalid() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        var exception = assertThrows(NoSuchElementException.class, () -> memberService.updateSBDStatus(
                1L, "asd", 1,1,1
        ));
        assertEquals(idMessage, exception.getMessage());

        verify(memberRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    void updateSBDStatus_ThrowsException_WhenEmailDoesntExist() {
        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(memberEntity1));
        when(memberRepository.findMemberByEmail(email)).thenReturn(null);

        var exception = assertThrows(NoSuchElementException.class, () -> memberService.updateSBDStatus(
                1L, email, 1,1,1
        ));
        assertEquals(emailDoesntExistMessage, exception.getMessage());

        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).findMemberByEmail(email);
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    void updateSBDStatus_ThrowsException_WhenEmailAndIdDontBelongToSameMember() {
        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(memberEntity1));
        when(memberRepository.findMemberByEmail(email)).thenReturn(memberEntity2);

        var exception = assertThrows(IllegalStateException.class, () -> memberService.updateSBDStatus(
                1L, email, 1,1,1
        ));
        assertEquals(differentMembers, exception.getMessage());
        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).findMemberByEmail(email);
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    void updateCompleteMember_SuccessfullyUpdatesMember_WhenCredentialsAreValid() {
        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(memberEntity1));
        when(memberRepository.findMemberByEmail(email)).thenReturn(memberEntity1);
        when(memberRepository.findMemberByEmail(memberEntity2.getEmail())).thenReturn(null);

        memberService.updateCompleteMember(1L, email, memberEntity2);

        assertThat(memberEntity1.equals(memberEntity2));

        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).findMemberByEmail(email);
        verify(memberRepository, times(1)).findMemberByEmail(memberEntity2.getEmail());
        verify(memberRepository, times(1)).save(memberEntity1);
    }

    @Test
    void updateCompleteMember_ThrowsException_WhenIdIsInvalid() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        var exception = assertThrows(NoSuchElementException.class, () ->
                memberService.updateCompleteMember(1L, email, memberEntity2));
        assertEquals(idMessage, exception.getMessage());

        verify(memberRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    void updateCompleteMember_ThrowsException_WhenEmailIsInvalid() {
        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(memberEntity1));

        var exception = assertThrows(IllegalArgumentException.class, () ->
                memberService.updateCompleteMember(1L, "", memberEntity2));
        assertEquals(invalidEmail, exception.getMessage());

        verify(memberRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    void updateCompleteMember_ThrowsException_WhenEmailDoesntExist() {
        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(memberEntity1));
        when(memberRepository.findMemberByEmail(email)).thenReturn(null);

        var exception = assertThrows(NoSuchElementException.class, () ->
                memberService.updateCompleteMember(1L, email, memberEntity2));
        assertEquals(emailDoesntExistMessage, exception.getMessage());

        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).findMemberByEmail(email);
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    void updateCompleteMember_ThrowsException_WhenEmailAndIdDontBelongToSameMember() {
        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(memberEntity1));
        when(memberRepository.findMemberByEmail(email)).thenReturn(memberEntity2);

        var exception = assertThrows(IllegalStateException.class, () ->
                memberService.updateCompleteMember(1L, email, memberEntity2));
        assertEquals(differentMembers, exception.getMessage());

        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).findMemberByEmail(email);
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    void updateCompleteMember_ThrowsException_WhenNewEmailAlreadyTaken() {
       MemberEntity memberEntity3 = memberEntity2;

        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(memberEntity1));
        when(memberRepository.findMemberByEmail(email)).thenReturn(memberEntity1);
        when(memberRepository.findMemberByEmail(memberEntity3.getEmail())).thenReturn(memberEntity1);

        var exception = assertThrows(IllegalStateException.class, () ->
                memberService.updateCompleteMember(1L, email, memberEntity3));
        assertEquals("The updated email that you are trying to give to " + memberEntity3.getName()
                + " is already registered under another member", exception.getMessage());

        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).findMemberByEmail(email);
        verify(memberRepository, times(1)).findMemberByEmail(memberEntity3.getEmail());
        verifyNoMoreInteractions(memberRepository);
    }

    //Error tests for update Role

}
