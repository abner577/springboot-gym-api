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
import practice.spring_gym_api.testdata.entity.MemberTestData;

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

    private MemberEntity memberEntity1;
    private MemberEntity memberEntity2;
    private MemberEntity memberEntity3;
    private MemberEntity memberEntity4;
    private MemberEntity memberEntity5;

    private String idMessage;
    private String name;
    private String email;
    private Set<MemberEntity> clients;

    @BeforeEach
    void setUp() {
        coachEntity1 = CoachTestData.createSeedCoach1();
        coachEntity2 = CoachTestData.createSeedCoach2();

        memberEntity1 = MemberTestData.createSeedMember1();
        memberEntity2 = MemberTestData.createSeedMember2();
        memberEntity3 = MemberTestData.createSeedMember3();
        memberEntity4 = MemberTestData.createSeedMember4();
        memberEntity5 = MemberTestData.createSeedMember5();


        idMessage = "Coach with an id of: " + 1L + " doesnt exist";
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
    void deleteCoachById_SuccessfullyDeletesCoach_WhenIdIsValid() {
        // Arrange
        Set<MemberEntity> memberEntities = coachEntity1.getClients();
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(coachEntity1));

        // Act
        coachService.deleteCoachById(1L);

        // Assert
        verify(coachRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).saveAll(memberEntities);
        verify(coachRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCoachById_SuccessfullyDeletesCoach_WhenCoachHasNoClients() {
        when(coachRepository.findById(1L)).thenReturn(Optional.ofNullable(fakeCoachEntity));

        coachService.deleteCoachById(1L);

        verify(coachRepository, times(1)).findById(1L);
        verify(coachRepository, times(1)).deleteById(1L);
        verify(memberRepository, never()).saveAll(any());
    }

    @Test
    void deleteCoachById_ThrowsException_WhenIdDoesntExist() {
        when(coachRepository.findById(1L)).thenReturn(Optional.empty());

        var exception = assertThrows(NoSuchElementException.class, () -> coachService.deleteCoachById(1L));
        assertEquals(idMessage, exception.getMessage());

        verify(coachRepository, never()).deleteById(1L);
        verify(memberRepository, never()).saveAll(any());
    }

    @Test
    void deleteAllCoaches_SuccessfullyDeletesAllCoaches() {
        List<MemberEntity> members = new ArrayList<>(List.of(memberEntity1, memberEntity2,
                memberEntity3, memberEntity4, memberEntity5));

        when(memberRepository.findAll()).thenReturn(members);
        when(coachRepository.findAll()).thenReturn(new ArrayList<>(List.of(coachEntity1, coachEntity2)));

        coachService.deleteAllCoaches();

        verify(memberRepository, times(1)).findAll();
        verify(coachRepository, times(1)).findAll();
        verify(memberRepository, times(1)).saveAll(members);
        verify(coachRepository, times(1)).deleteAll();
    }

    @Test
    void deleteAllCoaches_ThrowsException_WhenThereAreNoCoachesToDelete() {
        List<MemberEntity> members = new ArrayList<>(List.of(memberEntity1, memberEntity2,
                memberEntity3, memberEntity4, memberEntity5));

        when(memberRepository.findAll()).thenReturn(members);
        when(coachRepository.findAll()).thenReturn(new ArrayList<>());

        var exception = assertThrows(IllegalStateException.class, () -> coachService.deleteAllCoaches());
        assertEquals("No coaches left to delete", exception.getMessage());
    }
}
