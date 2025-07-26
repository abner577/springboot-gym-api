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
import practice.spring_gym_api.repository.CoachRepository;
import practice.spring_gym_api.repository.MemberRepository;
import practice.spring_gym_api.service.impl.MemberServiceimpl;
import practice.spring_gym_api.testdata.dto.MemberDTOTestData;
import practice.spring_gym_api.testdata.entity.CoachTestData;
import practice.spring_gym_api.testdata.entity.MemberTestData;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceDELETEUnitTest {
    @InjectMocks
    private MemberServiceimpl memberService;

    @Mock
    private MemberRepository memberRepository;

    private MemberEntity memberEntity1;
    private MemberEntity memberEntity2;
    private CoachEntity coachEntity1;

    @BeforeEach
    void setUp() {
        memberEntity1 = MemberTestData.createSeedMember1();
        memberEntity2 = MemberTestData.createSeedMember2();
        coachEntity1 = CoachTestData.createSeedCoach1();
    }

    @Test
    void deleteMemberById_SuccessfullyDeletesMember_WhenIdIsValid() {
        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(memberEntity1));

        memberService.deleteMemberById(1L);

        assertThat(memberEntity1.getCoachedBy() == null);
        assertFalse(memberRepository.existsById(1L));

        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).save(memberEntity1);
        verify(memberRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteMemberById_ThrowsException_WhenIdIsntValid() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        var exception = assertThrows(NoSuchElementException.class, () -> memberService.deleteMemberById(1L));
        assertEquals("Member with an id of: " + 1L + " doesnt exist", exception.getMessage());

        verify(memberRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    void deleteMembersBelowATotal_SuccessfullyDeletesMembers() {
        List<MemberEntity> list = List.of(memberEntity1, memberEntity2);
        when(memberRepository.findAll()).thenReturn(list);

        System.out.println(memberEntity1.getCoachedBy().getName());
        memberService.deleteMembersBelowATotal(10000);

        assertTrue(memberEntity1.getCoachedBy() == null);
        assertFalse(memberRepository.existsByEmail(memberEntity1.getEmail()));
        assertFalse(memberRepository.existsByEmail(memberEntity2.getEmail()));


        verify(memberRepository, times(1)).findAll();
        verify(memberRepository, times(1)).saveAll(list);
        verify(memberRepository, times(1)).deleteAll(list);
    }

    @Test
    void deleteMembersBelowATotal_ThrowsException_WHenTotalIsNegative() {
        var exception = assertThrows(IllegalArgumentException.class, () -> memberService.deleteMembersBelowATotal(-100));
        assertEquals("Total cannot be negative", exception.getMessage());

        verifyNoInteractions(memberRepository);
    }

}
