package practice.spring_gym_api.coach.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.enums.Roles;
import practice.spring_gym_api.repository.CoachRepository;
import practice.spring_gym_api.service.impl.CoachServiceimpl;
import practice.spring_gym_api.testdata.entity.CoachTestData;
import practice.spring_gym_api.testdata.invalidTestData.InvalidCoachEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CoachServicePOSTUnitTest {

    @InjectMocks
    CoachServiceimpl coachService;

    @Mock
    CoachRepository coachRepository;

    private CoachEntity coachEntity1;
    private CoachEntity coachEntity2;
    private CoachEntity invalidCoachEntity1;
    private CoachEntity fakeCoachEntity;
    private List<CoachEntity> coachEntityList;

    @BeforeEach
    void setUp() {
        coachEntity1 = CoachTestData.createSeedCoach1();
        coachEntity2 = CoachTestData.createSeedCoach2();
        invalidCoachEntity1 = InvalidCoachEntity.createInvalidSeedCoach1();
        coachEntityList = List.of(coachEntity1, coachEntity2);

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
    void registerNewCoach_SavesCoach_WhenEmailDoesntAlreadyExist(){
        // Arrange
        when(coachRepository.findByEmail(fakeCoachEntity.getEmail())).thenReturn(null);

        // Act
        coachService.registerNewCoach(fakeCoachEntity);

        // Assert
        verify(coachRepository, times(1)).save(fakeCoachEntity);
    }

    @Test
    void registerNewCoach_ThrowsException_WhenEmailAlreadyExists() {
        // Arrange
        when(coachRepository.findByEmail(fakeCoachEntity.getEmail())).thenReturn(fakeCoachEntity);

        // Act
       var exception = assertThrows(IllegalStateException.class, () -> coachService.registerNewCoach(fakeCoachEntity));
       assertEquals("Coach with an email of: " + fakeCoachEntity.getEmail() + " already exists"
       , exception.getMessage());

        // Assert
        verify(coachRepository, never()).save(any());
    }

    @Test
    void registerNewCoaches_SavesCoaches_WhenEmailDoesntAlreadyExist() {
        // Arrange
        when(coachRepository.existsByEmail(coachEntity1.getEmail())).thenReturn(false);
        when(coachRepository.existsByEmail(coachEntity2.getEmail())).thenReturn(false);

        // Act
        coachService.registerNewCoaches(coachEntityList);

        // Assert
        verify(coachRepository, times(1)).existsByEmail(coachEntity1.getEmail());
        verify(coachRepository, times(1)).existsByEmail(coachEntity2.getEmail());
        verify(coachRepository, times(1)).saveAll(coachEntityList);
    }

    @Test
    void registerNewCoaches_ThrowsException_WhenAnEmailAlreadyExist() {
        // Arrange
        when(coachRepository.existsByEmail(coachEntity1.getEmail())).thenReturn(true);

        // Act
        var exception = assertThrows(IllegalArgumentException.class, () -> coachService.registerNewCoaches(coachEntityList));
        assertEquals("Coach with an email of: " + coachEntity1.getEmail() + " already exists", exception.getMessage());

        // Assert
        verify(coachRepository, times(1)).existsByEmail(coachEntity1.getEmail());
        verify(coachRepository, never()).save(any());
    }


}
