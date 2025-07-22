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
import practice.spring_gym_api.entity.enums.Roles;
import practice.spring_gym_api.repository.CoachRepository;
import practice.spring_gym_api.repository.MemberRepository;
import practice.spring_gym_api.service.impl.CoachServiceimpl;
import practice.spring_gym_api.service.impl.MemberServiceimpl;
import practice.spring_gym_api.testdata.dto.MemberDTOTestData;
import practice.spring_gym_api.testdata.entity.CoachTestData;
import practice.spring_gym_api.testdata.entity.MemberTestData;
import practice.spring_gym_api.testdata.invalidTestData.InvalidCoachEntity;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceGETUnitTest {

    @InjectMocks
    private MemberServiceimpl memberService;

    @Mock
    private MemberRepository memberRepository;

    private MemberEntity memberEntity1;
    private MemberEntity memberEntity2;
    private MemberDTO memberDTO1;
    private MemberDTO memberDTO2;
    private CoachEntity coachEntity1;

    private String name;
    private String email;


    @BeforeEach
    void setup() {
        memberEntity1 = MemberTestData.createSeedMember1();
        memberEntity2 = MemberTestData.createSeedMember2();
        memberDTO1 = MemberDTOTestData.createdSeedMemberDTO1();
        memberDTO2 = MemberDTOTestData.createdSeedMemberDTO2();

        coachEntity1 = CoachTestData.createSeedCoach1();

        name = memberEntity1.getName();
        email = memberEntity1.getEmail();
    }

    @Test
    void getMemberById_SuccessfullyReturnsMemberEntity_WhenIdIsValid() throws Exception {
        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(memberEntity1));

        MemberEntity fromService = memberService.getMemberById(1L);

        MemberEntity copy = new MemberEntity(
                fromService.getName(), fromService.getDateOfBirth(),
                fromService.getMembershipDate(), fromService.getEmail(),
                fromService.getRole(), fromService.getBench(),
                fromService.getSquat(), fromService.getDeadlift(),
                fromService.getTotal()
        );

        System.out.println("Original: " + fromService);
        System.out.println("Copy: " + copy);

        System.out.println("== comparison: " + (fromService == copy));
        System.out.println(".equals() comparison: " + fromService.equals(copy));
    }

    @Test
    void getMemberById_ThrowsException_WhenIdIsNotValid() throws Exception {

    }


}
