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
import practice.spring_gym_api.repository.MemberRepository;
import practice.spring_gym_api.service.impl.CoachServiceimpl;
import practice.spring_gym_api.testdata.entity.CoachTestData;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CoachServiceDELETEUnitTest {
    @InjectMocks
    CoachServiceimpl coachService;

    @Mock
    CoachRepository coachRepository;

    @Mock
    MemberRepository memberRepository;

    private CoachEntity coachEntity1;
    private CoachEntity coachEntity2;
    private CoachEntity fakeCoachEntity;

    private String name;
    private String email;
    private Set<MemberEntity> clients;

    @BeforeEach
    void setUp() {
        coachEntity1 = CoachTestData.createSeedCoach1();
        coachEntity2 = CoachTestData.createSeedCoach2();

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

}
