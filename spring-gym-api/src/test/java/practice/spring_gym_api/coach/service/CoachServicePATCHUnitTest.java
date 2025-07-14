package practice.spring_gym_api.coach.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.entity.enums.Roles;
import practice.spring_gym_api.repository.CoachRepository;
import practice.spring_gym_api.repository.MemberRepository;
import practice.spring_gym_api.service.CoachService;
import practice.spring_gym_api.service.impl.CoachServiceimpl;
import practice.spring_gym_api.testdata.entity.CoachTestData;
import practice.spring_gym_api.testdata.invalidTestData.InvalidCoachEntity;

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

    private CoachEntity coachEntity1;
    private CoachEntity coachEntity2;
    private CoachEntity invalidCoachEntity1;
    private CoachEntity fakeCoachEntity;
    private List<CoachEntity> coachEntityList;

    private String name;
    private String email;
    private Set<MemberEntity> clients;

    @BeforeEach
    void setUp() {
        coachEntity1 = CoachTestData.createSeedCoach1();
        coachEntity2 = CoachTestData.createSeedCoach2();
        invalidCoachEntity1 = InvalidCoachEntity.createInvalidSeedCoach1();
        coachEntityList = List.of(coachEntity1, coachEntity2);

        name = coachEntity1.getName();
        email = coachEntity1.getEmail();
        clients = coachEntity1.getClients();

        fakeCoachEntity = new CoachEntity(
                3L,
                "Ginger Green",
                LocalDate.of(1970, 4, 12),
                Roles.ROLE_COACH,
                "gingerGreen@gmail.com",
                List.of("PPL", "Arnold"),
                "PEM-990X-YTR8"
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
        assertEquals("Coach with an id of: " + 1L + " doesnt exist", exception.getMessage());

        verify(coachRepository, times(1)).findById(1l);
        verifyNoInteractions(coachRepository);
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
        assertEquals("Email cannot be null or an empty string", exception.getMessage());

        verifyNoInteractions(coachRepository);
    }

    @Test
    void updateNameByIdAndEmail_ThrowsException_WhenEmailDoesntExist() {
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByEmail(email)).thenReturn(null);

        var exception = assertThrows(NoSuchElementException.class, () -> coachService.updateNameByIdAndEmail(1L, name, email));
        assertEquals("Coach with an email of: " + email + " doesnt exist", exception.getMessage());

        verify(coachRepository, times(1)).findById(1l);
        verify(coachRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void updateNameByIdAndEmail_ThrowsException_WhenIdAndEmailDontBelongToSameCoach() {
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByEmail(email)).thenReturn(coachEntity2);

        var exception = assertThrows(IllegalStateException.class, () -> coachService.updateNameByIdAndEmail(1L, name, email));
        assertEquals("Coach with an email of: " + email + " isnt the same coach with an id of: " + 1L
        , exception.getMessage());

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

        // Act
        coachService.addClientsByIdAndEmail(1L, email, coachEntity2.getClients());

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

        var exception = assertThrows(NoSuchElementException.class, () -> coachService.addClientsByIdAndEmail(1L, email, clients));
        assertEquals("Coach with an id of: " + 1L + " doesnt exist", exception.getMessage());

        verify(coachRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void addClientsByIdAndEmail_ThrowsException_WhenEmailIsInvalid() {
        var exception = assertThrows(IllegalArgumentException.class, () -> coachService.updateNameByIdAndEmail(1L, name, ""));
        assertEquals("Email cannot be null or an empty string", exception.getMessage());

        verifyNoInteractions(coachRepository);
    }

    @Test
    void addClientsByIdAndEmail_ThrowsException_WhenEmailDoesntExist() {
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByEmail(email)).thenReturn(null);

        var exception = assertThrows(NoSuchElementException.class, () -> coachService.addClientsByIdAndEmail(1L, email, clients));
        assertEquals("Coach with an email of: " + email + " doesnt exist", exception.getMessage());

        verify(coachRepository, times(1)).findById(1l);
        verify(coachRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void addClientsByIdAndEmail_ThrowsException_WhenNameAndEmailDontBelongToSameCoach() {
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByEmail(email)).thenReturn(coachEntity2);

        var exception = assertThrows(IllegalStateException.class, () -> coachService.updateNameByIdAndEmail(1L, name, email));
        assertEquals("Coach with an email of: " + email + " isnt the same coach with an id of: " + 1L
                , exception.getMessage());

        verify(coachRepository, times(1)).findById(1l);
        verify(coachRepository, times(1)).findByEmail(email);
        verify(coachRepository, never()).save(any());
    }

    @Test
    void replaceClientListByIdAndEmail_SuccessfullyReplacesClientList_WhenIdAndEmailAreValid() {
        Set<MemberEntity> oldList = new HashSet<>(clients);
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByEmail(email)).thenReturn(coachEntity1);

        System.out.println("Old client list:");
        for(MemberEntity memberEntity : clients) System.out.println("Client: " + memberEntity.getName());

        coachService.replaceClientListByIdAndEmail(1L, email, coachEntity2.getClients());

        System.out.println("New client list:");
        for(MemberEntity memberEntity : coachEntity1.getClients()) System.out.println("Client: " + memberEntity.getName());

        assertTrue(oldList != coachEntity1.getClients());

        verify(coachRepository, times(1)).findById(1l);
        verify(coachRepository, times(1)).findByEmail(email);
        verify(memberRepository, times(1)).saveAll(any());
    }

}
