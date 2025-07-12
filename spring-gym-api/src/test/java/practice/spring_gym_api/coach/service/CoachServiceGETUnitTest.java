package practice.spring_gym_api.coach.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.entity.enums.Roles;
import practice.spring_gym_api.repository.CoachRepository;
import practice.spring_gym_api.service.impl.CoachServiceimpl;
import practice.spring_gym_api.testdata.entity.CoachTestData;
import practice.spring_gym_api.testdata.invalidTestData.InvalidCoachEntity;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CoachServiceGETUnitTest {

    @InjectMocks
    CoachServiceimpl coachService;

    @Mock
    CoachRepository coachRepository;

    private CoachEntity coachEntity1;
    private CoachEntity coachEntity2;
    private CoachEntity fakeCoachEntity;


    @BeforeEach
    void setup() {
        coachEntity1 = CoachTestData.createSeedCoach1();
        coachEntity2 = CoachTestData.createSeedCoach2();
        fakeCoachEntity = InvalidCoachEntity.createInvalidSeedCoach1();

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
    void getCoachById_ReturnsCoach_WhenIdExists() {
        // Arrange
        Long id = coachEntity1.getId();
        when(coachRepository.findById(id)).thenReturn(Optional.ofNullable(coachEntity1));

        // Act
        coachService.getCoachById(id);

        // Assert
        verify(coachRepository, times(1)).findById(id);
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void getCoachById_ThrowsException_WhenIdDoesntExist() {
        // Arrange
        Long id = fakeCoachEntity.getId();
        when(coachRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        var exception = assertThrows(NoSuchElementException.class, () -> coachService.getCoachById(id));
        assertEquals("Coach with an id of: " + id + " doesnt exist", exception.getMessage());

        // Assert
        verify(coachRepository, times(1)).findById(id);
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void getAllCoaches_ReturnsAllCoaches_WhenCoachesExist() {
        // Arrange
        List<CoachEntity> coaches = List.of(coachEntity1, coachEntity2);
        System.out.println("List of coaches returned from repo: " +
                coaches.get(0).getName() + "," + coaches.get(1).getName());
        when(coachRepository.findAll()).thenReturn(coaches);

        // Act
        List<CoachEntity> actualCoaches = coachService.getAllCoaches();
        System.out.println("List of coaches returned from service: " +
                actualCoaches.get(0).getName() + "," + actualCoaches.get(1).getName());
        assertThat(actualCoaches)
                .hasSize(2)
                .containsExactlyInAnyOrder(coachEntity1, coachEntity2);

        // Assert
        verify(coachRepository, times(1)).findAll();
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void getAllCoaches_ThrowsExceptions_WhenNoCoachesExist() {
        // Arrange
        List<CoachEntity> emptyList = new ArrayList<>();
        when(coachRepository.findAll()).thenReturn(emptyList);

        System.out.println("List returned from coach repo: " + emptyList);

        // Act
        var exception = assertThrows(NoSuchElementException.class, () -> coachService.getAllCoaches());
        assertEquals("No coaches currently registered", exception.getMessage());

        // Assert
        verify(coachRepository, times(1)).findAll();
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void getCoachWithHighestClients_ReturnsCoachWithMostClients_WhenCoachesExist() {
        // Arrange
        List<CoachEntity> coaches = List.of(coachEntity1, coachEntity2);
        when(coachRepository.findAll()).thenReturn(coaches);

        System.out.println("Coach 1 stats: " + "\n" + "name: " + coachEntity1.getName() + "\n" +
                "clients : " + coachEntity1.getClients().size());
        System.out.println("Coach 2 stats: " + "\n" + "name: " + coachEntity2.getName() + "\n" +
                "clients : " + coachEntity2.getClients().size());

        // Act
        CoachEntity maxCoach = coachService.getCoachWithHighestClients();
        assertEquals(coachEntity1, maxCoach);

        // Assert
        verify(coachRepository, times(1)).findAll();
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void getCoachWithHighestClients_ThrowsException_WhenNoCoachesExist() {
        // Arrange
        List<CoachEntity> emptyList = new ArrayList<>();
        when(coachRepository.findAll()).thenReturn(emptyList);

        System.out.println("List contents retrieved from repo: " + emptyList);

        // Act
        var exception = assertThrows(NoSuchElementException.class, () -> coachService.getAllCoaches());
        assertEquals("No coaches currently registered", exception.getMessage());

        // Assert
        verify(coachRepository, times(1)).findAll();
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void getCoachWithLowestClients_ReturnsCoachWithLeastClients_WhenCoachesExist() {
        // Arrange
        List<CoachEntity> coaches = List.of(coachEntity1, coachEntity2);
        when(coachRepository.findAll()).thenReturn(coaches);

        System.out.println("Coach 1 stats: " + "\n" + "name: " + coachEntity1.getName() + "\n" +
                "clients : " + coachEntity1.getClients().size());
        System.out.println("Coach 2 stats: " + "\n" + "name: " + coachEntity2.getName() + "\n" +
                "clients : " + coachEntity2.getClients().size());

        // Act
        CoachEntity worstCoach = coachService.getCoachWithLowestClients();
        assertEquals(coachEntity2, worstCoach);

        // Assert
        verify(coachRepository, times(1)).findAll();
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void getCoachWithLowestClients_ThrowsException_WhenNoCoachesExist() {
        // Arrange
        List<CoachEntity> emptyList = new ArrayList<>();
        when(coachRepository.findAll()).thenReturn(emptyList);

        System.out.println("List contents retrieved from repo: " + emptyList);

        // Act
        var exception = assertThrows(NoSuchElementException.class, () -> coachService.getAllCoaches());
        assertEquals("No coaches currently registered", exception.getMessage());

        // Assert
        verify(coachRepository, times(1)).findAll();
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void getWorkoutPlansByCoachName_ReturnsWorkoutPlans_WhenCoachExists() {
        // Arrange
        String name = coachEntity1.getName();
        when(coachRepository.findCoachByName(name)).thenReturn(Optional.ofNullable(coachEntity1));
        System.out.println("Coach Entity found by repo: " + coachEntity1.getName());

        // Act
        List<String> workoutPlans = coachService.getWorkoutPlansByCoachName(name);
        assertEquals(coachEntity1.getWorkoutPlans(), workoutPlans);

        // Assert
        verify(coachRepository, times(1)).findCoachByName(name);
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void getWorkoutPlansByCoachName_ThrowsException_WhenCoachDoesntExists() {
        // Arrange
        String fakeName = fakeCoachEntity.getName();
        when(coachRepository.findCoachByName(fakeName)).thenReturn(Optional.empty());

        // Act
        var exception = assertThrows(NoSuchElementException.class, () -> coachService.getWorkoutPlansByCoachName(fakeName));
        assertEquals("Coach with a name of: " + fakeName + " doesn't exist", exception.getMessage());

        // Assert
        verify(coachRepository, times(1)).findCoachByName(fakeName);
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void getAllClientsByCoachId_ReturnsAllClientsOfACoach_WhenCoachExists() {
        // Arrange
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        System.out.println("Coach returned from repo: " + coachEntity1.getName());
        System.out.println("Coach client list size: " + coachEntity1.getClients().size());

        // Act
        Set<MemberEntity> entitySet = coachService.getAllClientsByCoachId(1L);
        assertEquals(coachEntity1.getClients(), entitySet);

        // Assert
        verify(coachRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void getAllClientsByCoachId_ThrowsException_WhenCoachDoesntExists() {
        // Arrange
        when(coachRepository.findById(4L)).thenReturn(Optional.empty());

        // Act
        var exception = assertThrows(NoSuchElementException.class, () -> coachService.getAllClientsByCoachId(4L));
        assertEquals("Coach with an id of: " + 4L + " doesnt exist", exception.getMessage());

        // Assert
        verify(coachRepository, times(1)).findById(4L);
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void getAllClientsByCoachId_ThrowsException_WhenCoachDoesntHaveClients() {
        // Arrange
        when(coachRepository.findById(3L)).thenReturn(Optional.ofNullable(fakeCoachEntity));

        // Act
        var exception = assertThrows(NoSuchElementException.class, () -> coachService.getAllClientsByCoachId(3L));
        assertEquals("Coach does not have any clients to access", exception.getMessage());

        // Assert
        verify(coachRepository, times(1)).findById(3L);
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void getAllCoachesThatAreAvaliable_ReturnsAllCoachesWithoutClients_WhenCoachExists() {
        List<CoachEntity> emptyList = new ArrayList<>();
        List<CoachEntity> list = List.of(coachEntity1, coachEntity2);
        when(coachRepository.findAll()).thenReturn(list);

        List<CoachEntity> listToReturn = coachService.getAllCoachesThatAreAvaliable();
        assertEquals(emptyList, listToReturn);

        verify(coachRepository, times(1)).findAll();
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void getAllCoachesThatAreAvaliable_ThrowsException_WhenNoCoachExist() {
        List<CoachEntity> emptyList = new ArrayList<>();
        when(coachRepository.findAll()).thenReturn(emptyList);

        var exception = assertThrows(NoSuchElementException.class, () -> coachService.getAllCoachesThatAreAvaliable());
        assertEquals("No coaches currently registered", exception.getMessage());

        verify(coachRepository, times(1)).findAll();
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void getCoachByCoachCode_ReturnsCoach_WhenCoachCodeExists() {
        String code = coachEntity1.getCoachCode();
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByCoachCode(code)).thenReturn(coachEntity1);

        CoachEntity coachToReturn = coachService.getCoachByCoachCode(1L, code);
        assertEquals(coachEntity1, coachToReturn);

        verify(coachRepository, times(1)).findById(1L);
        verify(coachRepository, times(1)).findByCoachCode(code);
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void getCoachByCoachCode_ThrowsException_WhenCoachIdDoesntExist() {
        String code = coachEntity1.getCoachCode();
        when(coachRepository.findById(1L)).thenReturn(Optional.empty());

        var exception = assertThrows(NoSuchElementException.class, () -> coachService.getCoachByCoachCode(1L, code));
        assertEquals("Coach with an id of: " + 1L + " doesnt exist", exception.getMessage());

        verify(coachRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void getCoachByCoachCode_ThrowsException_WhenCoachCodeDoesntExist() {
        String code = coachEntity1.getCoachCode();
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByCoachCode(code)).thenReturn(null);

        var exception = assertThrows(NoSuchElementException.class, () -> coachService.getCoachByCoachCode(1L, code));
        assertEquals("Coach with a code of: " + code + " doesnt exist", exception.getMessage());

        verify(coachRepository, times(1)).findById(1L);
        verify(coachRepository, times(1)).findByCoachCode(code);
        verifyNoMoreInteractions(coachRepository);
    }

    @Test
    void getCoachByCoachCode_ThrowsException_WhenCoachCodeAndIdDontBelongToSameCoach() {
        String code = coachEntity2.getCoachCode();
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));
        when(coachRepository.findByCoachCode(code)).thenReturn(coachEntity2);

        var exception = assertThrows(IllegalStateException.class, () -> coachService.getCoachByCoachCode(1L, code));
        assertEquals("Coach with an id of: " + 1L + " isnt the same coach with a coach code of: " + code, exception.getMessage());

        verify(coachRepository, times(1)).findById(1L);
        verify(coachRepository, times(1)).findByCoachCode(code);
        verifyNoMoreInteractions(coachRepository);
    }
}
