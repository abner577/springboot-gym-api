package practice.spring_gym_api.coach.service;


import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import practice.spring_gym_api.dto.CoachMapper;
import practice.spring_gym_api.dto.request.CoachRequestDTO;
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

    @Mock
    CoachMapper coachMapper;

    private CoachEntity coachEntity1;
    private CoachEntity coachEntity2;
    private CoachRequestDTO coachRequestDTO1;
    private CoachRequestDTO coachRequestDTO2;


    private CoachEntity invalidCoachEntity1;
    private CoachEntity fakeCoachEntity;
    private CoachRequestDTO fakeCoachRequestDTO;
    private List<CoachEntity> coachEntityList;
    private List<CoachRequestDTO> coachRequestDTOList;

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

        fakeCoachRequestDTO = new CoachRequestDTO(
                "Ginger Green",
                "gingerGreen@gmail.com",
                LocalDate.of(1970, 4, 12),
                Roles.ROLE_COACH,
                List.of("PPL", "Arnold")
        );

        coachRequestDTO1 = new CoachRequestDTO(
                coachEntity1.getName(), coachEntity1.getEmail(), coachEntity1.getDateOfBirth(),
                coachEntity1.getRole(), coachEntity1.getWorkoutPlans()
        );

        coachRequestDTO2 = new CoachRequestDTO(
                coachEntity2.getName(), coachEntity2.getEmail(), coachEntity2.getDateOfBirth(),
                coachEntity2.getRole(), coachEntity2.getWorkoutPlans()
        );

        coachRequestDTOList = List.of(coachRequestDTO1, coachRequestDTO2);
    }

    @Test
    void registerNewCoach_SavesCoach_WhenEmailDoesntAlreadyExist(){
        // Arrange
        when(coachRepository.findByEmail(fakeCoachRequestDTO.getEmail())).thenReturn(null);
        when(coachMapper.convertToCoachEntity(fakeCoachRequestDTO)).thenReturn(fakeCoachEntity);

        // Act
        coachService.registerNewCoach(fakeCoachRequestDTO);

        // Assert
        verify(coachRepository, times(1)).save(fakeCoachEntity);
    }

    @Test
    void registerNewCoach_ThrowsException_WhenEmailAlreadyExists() {
        // Arrange
        when(coachRepository.findByEmail(fakeCoachRequestDTO.getEmail())).thenReturn(fakeCoachEntity);

        // Act
       var exception = assertThrows(IllegalStateException.class, () -> coachService.registerNewCoach(fakeCoachRequestDTO));
       assertEquals("Coach with an email of: " + fakeCoachEntity.getEmail() + " already exists"
       , exception.getMessage());

        // Assert
        verify(coachRepository, never()).save(any());
    }

    @Test
    void registerNewCoaches_SavesCoaches_WhenEmailDoesntAlreadyExist() {
        // Arrange
        when(coachRepository.existsByEmail(coachRequestDTO1.getEmail())).thenReturn(false);
        when(coachRepository.existsByEmail(coachRequestDTO2.getEmail())).thenReturn(false);
        when(coachMapper.convertToCoachEntity(coachRequestDTO1)).thenReturn(coachEntity1);
        when(coachMapper.convertToCoachEntity(coachRequestDTO2)).thenReturn(coachEntity2);

        // Act
        coachService.registerNewCoaches(coachRequestDTOList);

        // Assert
        verify(coachRepository, times(1)).existsByEmail(coachRequestDTO1.getEmail());
        verify(coachRepository, times(1)).existsByEmail(coachRequestDTO2.getEmail());
        verify(coachRepository, times(1)).saveAll(coachEntityList);
    }

    @Test
    void registerNewCoaches_ThrowsException_WhenAnEmailAlreadyExist() {
        // Arrange
        when(coachRepository.existsByEmail(coachRequestDTO1.getEmail())).thenReturn(true);

        // Act
        var exception = assertThrows(IllegalArgumentException.class, () -> coachService.registerNewCoaches(coachRequestDTOList));
        assertEquals("Coach with an email of: " + coachRequestDTO1.getEmail() + " already exists", exception.getMessage());

        // Assert
        verify(coachRepository, times(1)).existsByEmail(coachRequestDTO1.getEmail());
        verify(coachRepository, never()).save(any());
    }
}
