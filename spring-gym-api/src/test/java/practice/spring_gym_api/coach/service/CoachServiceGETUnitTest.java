package practice.spring_gym_api.coach.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import practice.spring_gym_api.dto.CoachMapper;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.enums.Roles;
import practice.spring_gym_api.repository.CoachRepository;
import practice.spring_gym_api.repository.MemberRepository;
import practice.spring_gym_api.repository.WorkerRepository;
import practice.spring_gym_api.service.impl.CoachServiceimpl;
import practice.spring_gym_api.testdata.entity.CoachTestData;
import practice.spring_gym_api.testdata.invalidTestData.InvalidCoachEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    }

    @Test
    void getCoachById_ThrowsException_WhenIdDoesntExist() {
        // Arrange
        Long id = coachEntity1.getId();
        when(coachRepository.findById(id)).thenReturn(null);

        // Act
       var exception = assertThrows(NoSuchElementException.class, () -> coachService.getCoachById(id));
        assertEquals("Coach with an id of: " + id + " doesnt exist", exception);


        // Assert
        verify(coachRepository, times(1)).findById(id);
    }
}
