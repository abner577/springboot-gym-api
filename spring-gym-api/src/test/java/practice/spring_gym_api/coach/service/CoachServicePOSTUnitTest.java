package practice.spring_gym_api.coach.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import practice.spring_gym_api.controller.CoachController;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.enums.Roles;
import practice.spring_gym_api.repository.CoachRepository;
import practice.spring_gym_api.repository.MemberRepository;
import practice.spring_gym_api.repository.WorkerRepository;
import practice.spring_gym_api.service.CoachService;
import practice.spring_gym_api.testdata.entity.CoachTestData;
import practice.spring_gym_api.testdata.invalidTestData.InvalidCoachEntity;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CoachServicePOSTUnitTest {

    @InjectMocks
    CoachService coachService;

    @MockitoBean
    CoachController coachController;

    @MockitoBean
    CoachRepository coachRepository;

    @MockitoBean
    MemberRepository memberRepository;

    @MockitoBean
    WorkerRepository workerRepository;

    private CoachEntity coachEntity1;
    private CoachEntity coachEntity2;
    private CoachEntity invalidCoachEntity1;
    private CoachEntity fakeCoachEntity;

    @BeforeEach
    void setUp() {
        coachEntity1 = CoachTestData.createSeedCoach1();
        coachEntity2 = CoachTestData.createSeedCoach2();
        invalidCoachEntity1 = InvalidCoachEntity.createInvalidSeedCoach1();

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

}
