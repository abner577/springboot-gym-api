package practice.spring_gym_api.coach.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import practice.spring_gym_api.dto.impl.CoachMapperimpl;
import practice.spring_gym_api.dto.request.CoachRequestDTO;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.entity.enums.Roles;
import practice.spring_gym_api.repository.CoachRepository;
import practice.spring_gym_api.repository.MemberRepository;
import practice.spring_gym_api.repository.WorkerRepository;
import practice.spring_gym_api.service.impl.CoachServiceimpl;
import practice.spring_gym_api.testdata.entity.CoachTestData;
import practice.spring_gym_api.testdata.entity.MemberTestData;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CoachServicePATCHUnitTest {

    @InjectMocks
    CoachServiceimpl coachService;

    @Mock
    CoachRepository coachRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    WorkerRepository workerRepository;

    @Mock(name = "coachMapperimpl")
    CoachMapperimpl coachMapperimpl;

    private CoachEntity coachEntity1;
    private CoachEntity coachEntity2;
    private CoachEntity fakeCoachEntity;
    private CoachRequestDTO coachRequestDTO1;
    private CoachRequestDTO coachRequestDTO2;

    private MemberEntity memberEntity2;
    private MemberEntity memberEntity4;

    private String name;
    private String email;
    private Set<MemberEntity> clients;
    private String idMessage;
    private String emailMessage;
    private String notSameCoachMessage;
    private String invalidEmailMessage;

    private List<String> client1Emails;
    private List<String> client2Emails;

    @BeforeEach
    void setUp() {
        coachEntity1 = CoachTestData.createSeedCoach1();
        coachEntity2 = CoachTestData.createSeedCoach2();

        name = coachEntity1.getName();
        email = coachEntity1.getEmail();
        clients = coachEntity1.getClients();

        memberEntity2 = MemberTestData.createSeedMember2();
        memberEntity4 = MemberTestData.createSeedMember4();

        idMessage = "Coach with an id of: " + 1L + " doesnt exist";
        emailMessage = "Coach with an email of: " + email + " doesnt exist";
        notSameCoachMessage = "Coach with an email of: " + email + " isnt the same coach with an id of: " + 1L;
        invalidEmailMessage = "Email cannot be null or an empty string";

        client1Emails = List.of("johnDoe@gmail.com", "davidLee@gmail.com", "carlosRivera@gmail.com");
        client2Emails = List.of("janeSmith@gmail.com", "emelyChen@gmail.com");

        fakeCoachEntity = new CoachEntity(
                3L,
                "Ginger Green",
                LocalDate.of(1970, 4, 12),
                Roles.ROLE_COACH,
                "gingerGreen@gmail.com",
                List.of("PPL", "Arnold"),
                "PEM-990X-YTR8"
        );

        coachRequestDTO1 = new CoachRequestDTO(
                coachEntity1.getName(), coachEntity1.getEmail(), coachEntity1.getDateOfBirth(),
                coachEntity1.getRole(), coachEntity1.getWorkoutPlans(), coachEntity1.getCoachCode()
        );

        coachRequestDTO2 = new CoachRequestDTO(
                coachEntity2.getName(), coachEntity2.getEmail(), coachEntity2.getDateOfBirth(),
                coachEntity2.getRole(), coachEntity2.getWorkoutPlans(), coachEntity2.getCoachCode()
        );
    }

    @Test
    void updateNameByIdAndEmail_SuccessfullyUpdatesName_WhenIdAndEmailAreValid() {
        // Arrange
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByEmail(email)).thenReturn(coachEntity1);

        // Act
        coachService.updateNameByIdAndEmail(1l, fakeCoachEntity.getName(), email);
        assertEquals(fakeCoachEntity.getName(), coachEntity1.getName());
        System.out.println("Old name: " + name + " New name: " + coachEntity1.getName());

        // Assert
        verify(coachRepository, times(1)).findById(1L);
        verify(coachRepository, times(1)).findByEmail(email);
        verify(coachRepository, times(1)).save(any());
    }

    @Test
    void updateNameByIdAndEmail_ThrowsException_WhenIdDoesntExist() {
        when(coachRepository.findById(1L)).thenReturn(Optional.empty());

        var exception = assertThrows(NoSuchElementException.class, () -> coachService.updateNameByIdAndEmail(1L, name, email));
        assertEquals(idMessage, exception.getMessage());

        verify(coachRepository, times(1)).findById(1l);
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void updateNameByIdAndEmail_ThrowsException_WhenNameIsInvalid() {
        var exception = assertThrows(IllegalArgumentException.class, () -> coachService.updateNameByIdAndEmail(1L, "", email));
        assertEquals("Name cannot be null or an empty string", exception.getMessage());

        verifyNoInteractions(coachRepository);
    }

    @Test
    void updateNameByIdAndEmail_ThrowsException_WhenEmailIsInvalid() {
        var exception = assertThrows(IllegalArgumentException.class, () -> coachService.updateNameByIdAndEmail(1L, name, ""));
        assertEquals(invalidEmailMessage, exception.getMessage());

        verifyNoInteractions(coachRepository);
    }

    @Test
    void updateNameByIdAndEmail_ThrowsException_WhenEmailDoesntExist() {
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByEmail(email)).thenReturn(null);

        var exception = assertThrows(NoSuchElementException.class, () -> coachService.updateNameByIdAndEmail(1L, name, email));
        assertEquals(emailMessage, exception.getMessage());

        verify(coachRepository, times(1)).findById(1l);
        verify(coachRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void updateNameByIdAndEmail_ThrowsException_WhenIdAndEmailDontBelongToSameCoach() {
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByEmail(email)).thenReturn(coachEntity2);

        var exception = assertThrows(IllegalStateException.class, () -> coachService.updateNameByIdAndEmail(1L, name, email));
        assertEquals(notSameCoachMessage, exception.getMessage());

        verify(coachRepository, times(1)).findById(1l);
        verify(coachRepository, times(1)).findByEmail(email);
        verify(coachRepository, never()).save(any());
    }

    @Test
    void addClientsByIdAndEmail_SuccessfullyAddsClient_WhenIdAndEmailAreValid() {
        // Arrange
        Set<MemberEntity> oldClientList = new HashSet<>(coachEntity1.getClients());
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByEmail(email)).thenReturn(coachEntity1);
        when(memberRepository.findMemberByEmail("janeSmith@gmail.com")).thenReturn(memberEntity2);
        when(memberRepository.findMemberByEmail("emelyChen@gmail.com")).thenReturn(memberEntity4);

        // Act
        coachService.addClientsByIdAndEmail(1L, email, client2Emails);

        // Assert
        assertTrue(oldClientList != coachEntity1.getClients());
        assertTrue(coachEntity1.getClients().size() > oldClientList.size());

        verify(coachRepository, times(1)).findById(1l);
        verify(coachRepository, times(1)).findByEmail(email);
        verify(coachRepository, times(1)).save(any());
    }

    @Test
    void addClientsByIdAndEmail_ThrowsException_WhenIdDoesntExist() {
        when(coachRepository.findById(1L)).thenReturn(Optional.empty());

        var exception = assertThrows(NoSuchElementException.class, () -> coachService.addClientsByIdAndEmail(1L, email, client1Emails));
        assertEquals(idMessage, exception.getMessage());

        verify(coachRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void addClientsByIdAndEmail_ThrowsException_WhenEmailIsInvalid() {
        var exception = assertThrows(IllegalArgumentException.class, () -> coachService.addClientsByIdAndEmail(1L, "", client1Emails));
        assertEquals(invalidEmailMessage, exception.getMessage());

        verifyNoInteractions(coachRepository);
    }

    @Test
    void addClientsByIdAndEmail_ThrowsException_WhenEmailDoesntExist() {
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByEmail(email)).thenReturn(null);

        var exception = assertThrows(NoSuchElementException.class, () -> coachService.addClientsByIdAndEmail(1L, email, client1Emails));
        assertEquals(emailMessage, exception.getMessage());

        verify(coachRepository, times(1)).findById(1l);
        verify(coachRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void replaceClientListByIdAndEmail_ThrowsException_WhenEmailIsInvalid() {
        List<Long> idsOfClients = new ArrayList<>();
        for(MemberEntity member : clients) idsOfClients.add(member.getId());

        var exception = assertThrows(IllegalArgumentException.class, () ->
                coachService.replaceClientListByIdAndEmail(1L, "", idsOfClients));
        assertEquals(invalidEmailMessage, exception.getMessage());

        verifyNoInteractions(coachRepository);
    }

    @Test
    void replaceClientListByIdAndEmail_ThrowsException_WhenIdListIsInvalid() {
        List<Long> idsOfClients = new ArrayList<>();
        for(MemberEntity member : clients) idsOfClients.add(member.getId());

        var exception = assertThrows(IllegalArgumentException.class, () ->
                coachService.replaceClientListByIdAndEmail(1L, "example@example.com", new ArrayList<>()));
        assertEquals("Id list cannot be empty", exception.getMessage());

        verifyNoInteractions(coachRepository);
    }

    @Test
    void replaceClientListByIdAndEmail_ThrowsException_WhenIdDoesntExist() {
        List<Long> idsOfClients = new ArrayList<>();
        for(MemberEntity member : clients) idsOfClients.add(member.getId());

        when(coachRepository.findById(1L)).thenReturn(Optional.empty());
        var exception = assertThrows(NoSuchElementException.class, () ->
                coachService.replaceClientListByIdAndEmail(1L, email, idsOfClients));
        assertEquals(idMessage, exception.getMessage());

        verify(coachRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void replaceClientListByIdAndEmail_ThrowsException_WhenEmailDoesntExist() {
        List<Long> idsOfClients = new ArrayList<>();
        for(MemberEntity member : clients) idsOfClients.add(member.getId());

        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByEmail(email)).thenReturn(null);

        var exception = assertThrows(NoSuchElementException.class, () ->
                coachService.replaceClientListByIdAndEmail(1L, email, idsOfClients));
        assertEquals(emailMessage, exception.getMessage());

        verify(coachRepository, times(1)).findById(1L);
        verify(coachRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void replaceClientListByIdAndEmail_ThrowsException_WhenNameAndEmailDontBelongToSameCoach() {
        List<Long> idsOfClients = new ArrayList<>();
        for(MemberEntity member : clients) idsOfClients.add(member.getId());

        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByEmail(email)).thenReturn(coachEntity2);

        var exception = assertThrows(IllegalStateException.class, () -> coachService.replaceClientListByIdAndEmail(1L, email, idsOfClients));
        assertEquals(notSameCoachMessage, exception.getMessage());

        verify(coachRepository, times(1)).findById(1l);
        verify(coachRepository, times(1)).findByEmail(email);
        verify(memberRepository, never()).saveAll(any());
    }

    @Test
    void replaceClientListByIdAndEmail_ThrowsException_WhenUpdatedClientsDontExist() {
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByEmail(email)).thenReturn(coachEntity1);
        when(memberRepository.findById(15L)).thenReturn(Optional.empty());

        var exception = assertThrows(NoSuchElementException.class, () -> coachService.replaceClientListByIdAndEmail(1L, email, List.of(15L)));
        assertEquals("Member with an id of: " + 15L + " doesnt exist", exception.getMessage());

        verify(coachRepository, times(1)).findById(1l);
        verify(coachRepository, times(1)).findByEmail(email);
        verify(memberRepository, times(1)).findById(15L);
        verify(memberRepository, never()).saveAll(any());
    }

    @Test
    void updateWorkoutPlans_SuccessfullyUpdatesWorkout_WhenIdAndEmailAreValid() {
        List<String> oldPlans = new ArrayList<>(coachEntity1.getWorkoutPlans());
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByEmail(email)).thenReturn(coachEntity1);

        coachService.updateWorkoutPlans(1L, email, coachEntity2.getWorkoutPlans());
        assertTrue(oldPlans != coachEntity1.getWorkoutPlans());

        verify(coachRepository, times(1)).findById(1l);
        verify(coachRepository, times(1)).findByEmail(email);
        verify(coachRepository, times(1)).save(any());
    }

    @Test
    void updateWorkoutPlans_ThrowsException_WhenEmailIsInvalid() {
        var exception = assertThrows(IllegalArgumentException.class, () ->
                coachService.updateWorkoutPlans(1L, "", coachEntity2.getWorkoutPlans()));
        assertEquals(invalidEmailMessage, exception.getMessage());

        verifyNoInteractions(coachRepository);
    }

    @Test
    void updateWorkoutPlans_ThrowsException_WhenIdDoesntExist() {
        when(coachRepository.findById(1L)).thenReturn(Optional.empty());
        var exception = assertThrows(NoSuchElementException.class, () ->
                coachService.updateWorkoutPlans(1L, email, coachEntity2.getWorkoutPlans()));
        assertEquals(idMessage, exception.getMessage());

        verify(coachRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void updateWorkoutPlans_ThrowsException_WhenEmailDoesntExist() {
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByEmail(email)).thenReturn(null);

        var exception = assertThrows(NoSuchElementException.class, () ->
                coachService.updateWorkoutPlans(1L, email, coachEntity2.getWorkoutPlans()));
        assertEquals(emailMessage, exception.getMessage());

        verify(coachRepository, times(1)).findById(1L);
        verify(coachRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void updateWorkoutPlans_ThrowsException_WhenNameAndEmailDontBelongToSameCoach() {
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByEmail(email)).thenReturn(coachEntity2);

        var exception = assertThrows(IllegalStateException.class, () ->  coachService.updateWorkoutPlans(1L, email, coachEntity2.getWorkoutPlans()));
        assertEquals(notSameCoachMessage, exception.getMessage());

        verify(coachRepository, times(1)).findById(1l);
        verify(coachRepository, times(1)).findByEmail(email);
        verify(coachRepository, never()).save(any());
    }

    @Test
    void updateCoachByIdAndEmail_SuccessfullyUpdatesCoach_WhenIdAndEmailAreValid() {
        String name = new String(coachEntity1.getName());
        String email = new String(coachEntity1.getEmail());
        String newEmail = coachEntity2.getEmail();

        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByEmail(email)).thenReturn(coachEntity1);
        when(coachRepository.findByEmail(newEmail)).thenReturn(null);

        coachService.updateCoachByIdAndEmail(1L, email, coachRequestDTO2);

        assertTrue(name != coachEntity1.getName());
        assertTrue(email != coachEntity1.getEmail());

        verify(coachRepository, times(1)).findById(1L);
        verify(coachRepository, times(1)).findByEmail(email);
        verify(coachRepository, times(1)).findByEmail(newEmail);
        verify(coachRepository, times(1)).save(any());
    }

    @Test
    void updateCoachByIdAndEmail_ThrowsException_WhenEmailIsInvalid() {
        var exception = assertThrows(IllegalArgumentException.class, () ->
                coachService.updateCoachByIdAndEmail(1L, "", coachRequestDTO2));
        assertEquals(invalidEmailMessage, exception.getMessage());

        verifyNoInteractions(coachRepository);
    }

    @Test
    void updateCoachByIdAndEmail_ThrowsException_WhenIdDoesntExist() {
        when(coachRepository.findById(1L)).thenReturn(Optional.empty());
        var exception = assertThrows(NoSuchElementException.class, () ->
                coachService.updateCoachByIdAndEmail(1L, email, coachRequestDTO2));
        assertEquals(idMessage, exception.getMessage());

        verify(coachRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void updateCoachByIdAndEmail_ThrowsException_WhenEmailDoesntExist() {
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByEmail(email)).thenReturn(null);

        var exception = assertThrows(NoSuchElementException.class, () ->
                coachService.updateCoachByIdAndEmail(1L, email, coachRequestDTO2));
        assertEquals(emailMessage, exception.getMessage());

        verify(coachRepository, times(1)).findById(1L);
        verify(coachRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void updateCoachByIdAndEmail_ThrowsException_WhenNameAndEmailDontBelongToSameCoach() {
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByEmail(email)).thenReturn(coachEntity2);

        var exception = assertThrows(IllegalStateException.class, () ->  coachService.updateCoachByIdAndEmail(1L, email, coachRequestDTO2));
        assertEquals(notSameCoachMessage, exception.getMessage());

        verify(coachRepository, times(1)).findById(1l);
        verify(coachRepository, times(1)).findByEmail(email);
        verify(coachRepository, never()).save(any());
    }

    @Test
    void updateCoachByIdAndEmail_ThrowsException_WhenNewEmailIsAlreadyTaken() {
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByEmail(email)).thenReturn(coachEntity1);
        when(coachRepository.findByEmail(coachEntity2.getEmail())).thenReturn(coachEntity2);

        var exception = assertThrows(IllegalArgumentException.class, () ->  coachService.updateCoachByIdAndEmail(1L, email, coachRequestDTO2));
        assertEquals("The updated email that you are trying to give to " + name + " is already registered under another coach"
                , exception.getMessage());

        verify(coachRepository, times(1)).findById(1l);
        verify(coachRepository, times(1)).findByEmail(email);
        verify(coachRepository, times(1)).findByEmail(coachEntity2.getEmail());
        verify(coachRepository, never()).save(any());
    }

    @Test
    void updateRoleOfACoach_ThrowsException_WhenEmailIsInvalid() {
        var exception = assertThrows(IllegalArgumentException.class,
                () -> coachService.updateRoleOfACoach(1L, "", "ROLE_COACH"));
        assertEquals(invalidEmailMessage, exception.getMessage());

        verifyNoInteractions(coachRepository, memberRepository, workerRepository, coachMapperimpl);
    }

    @Test
    void updateRoleOfACoach_ThrowsException_WhenIdDoesntExist() {
        when(coachRepository.findById(1L)).thenReturn(Optional.empty());

        var exception = assertThrows(NoSuchElementException.class,
                () -> coachService.updateRoleOfACoach(1L, email, "ROLE_WORKER"));
        assertEquals(idMessage, exception.getMessage());

        verify(coachRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(coachRepository);
        verifyNoInteractions(memberRepository, workerRepository, coachMapperimpl);
    }

    @Test
    void updateRoleOfACoach_ThrowsException_WhenEmailDoesntExist() {
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByEmail(email)).thenReturn(null);

        var exception = assertThrows(NoSuchElementException.class,
                () -> coachService.updateRoleOfACoach(1L, email, "ROLE_WORKER"));
        assertEquals(emailMessage, exception.getMessage());

        verify(coachRepository, times(1)).findById(1L);
        verify(coachRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(coachRepository);
        verifyNoInteractions(memberRepository, workerRepository, coachMapperimpl);
    }

    @Test
    void updateRoleOfACoach_ThrowsException_WhenNameAndEmailDontBelongToSameCoach() {
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByEmail(email)).thenReturn(coachEntity2);

        var exception = assertThrows(IllegalStateException.class,
                () -> coachService.updateRoleOfACoach(1L, email, "ROLE_WORKER"));
        assertEquals(notSameCoachMessage, exception.getMessage());

        verify(coachRepository, times(1)).findById(1L);
        verify(coachRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(coachRepository);
        verifyNoInteractions(memberRepository, workerRepository, coachMapperimpl);
    }

    @Test
    void updateRoleOfACoach_ThrowsException_WhenNewRoleEqualToCoach() {
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByEmail(email)).thenReturn(coachEntity1);

        var exception = assertThrows(IllegalArgumentException.class,
                () -> coachService.updateRoleOfACoach(1L, email, "ROLE_COACH"));
        assertEquals("Coach: " + name + " already has a role of ROLE_COACH", exception.getMessage());

        verify(coachRepository, times(1)).findById(1L);
        verify(coachRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(coachRepository);
        verifyNoInteractions(memberRepository, workerRepository, coachMapperimpl);
    }

    @Test
    void updateRoleOfACoach_ThrowsException_WhenNewRoleIsInvalid() {
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByEmail(email)).thenReturn(coachEntity1);

        var exception = assertThrows(IllegalStateException.class,
                () -> coachService.updateRoleOfACoach(1L, email, "ROLE_FAKE"));
        assertEquals("Role must be either ROLE_COACH, ROLE_WORKER, or ROLE_MEMBER", exception.getMessage());

        verify(coachRepository, times(1)).findById(1L);
        verify(coachRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(coachRepository);
        verifyNoInteractions(memberRepository, workerRepository, coachMapperimpl);
    }

    @Test
    void updateCodeOfACoach_SuccessfullyUpdatesCode_WhenIdAndEmailAreValid() {
        String oldCode = new String(coachEntity1.getCoachCode());
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByEmail(email)).thenReturn(coachEntity1);
        when(coachRepository.findByCoachCode(coachEntity2.getCoachCode())).thenReturn(null);

        coachService.updateCodeOfACoach(1L, email, coachEntity2.getCoachCode());
        assertNotEquals(oldCode, coachEntity1.getCoachCode());

        verify(coachRepository, times(1)).findById(1l);
        verify(coachRepository, times(1)).findByEmail(email);
        verify(coachRepository, times(1)).findByCoachCode(coachEntity1.getCoachCode());
        verify(coachRepository, times(1)).save(any());
    }

    @Test
    void updateCodeOfACoach_ThrowsException_WhenEmailIsInvalid() {
        var exception = assertThrows(IllegalArgumentException.class, () ->
                coachService.updateCodeOfACoach(1L, "", coachEntity2.getCoachCode()));
        assertEquals(invalidEmailMessage, exception.getMessage());

        verifyNoInteractions(coachRepository);
    }

    @Test
    void updateCodeOfACoach_ThrowsException_WhenIdDoesntExist() {
        when(coachRepository.findById(1L)).thenReturn(Optional.empty());
        var exception = assertThrows(NoSuchElementException.class, () ->
                coachService.updateCodeOfACoach(1L, email, coachEntity2.getCoachCode()));
        assertEquals(idMessage, exception.getMessage());

        verify(coachRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void updateCodeOfACoach_ThrowsException_WhenEmailDoesntExist() {
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByEmail(email)).thenReturn(null);

        var exception = assertThrows(NoSuchElementException.class, () ->
                coachService.updateCodeOfACoach(1L, email, coachEntity2.getCoachCode()));
        assertEquals(emailMessage, exception.getMessage());

        verify(coachRepository, times(1)).findById(1L);
        verify(coachRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void updateCodeOfACoach_ThrowsException_WhenNameAndEmailDontBelongToSameCoach() {
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByEmail(email)).thenReturn(coachEntity2);

        var exception = assertThrows(IllegalStateException.class, () ->  coachService.updateCodeOfACoach(1L, email, coachEntity2.getCoachCode()));
        assertEquals(notSameCoachMessage, exception.getMessage());

        verify(coachRepository, times(1)).findById(1l);
        verify(coachRepository, times(1)).findByEmail(email);
        verify(coachRepository, never()).save(any());
    }
}
