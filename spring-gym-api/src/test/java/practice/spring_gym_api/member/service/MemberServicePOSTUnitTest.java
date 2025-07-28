package practice.spring_gym_api.member.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.entity.enums.Roles;
import practice.spring_gym_api.repository.MemberRepository;
import practice.spring_gym_api.service.impl.MemberServiceimpl;
import practice.spring_gym_api.testdata.entity.CoachTestData;
import practice.spring_gym_api.testdata.entity.MemberTestData;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServicePOSTUnitTest {

    @InjectMocks
    private MemberServiceimpl memberService;

    @Mock
    private MemberRepository memberRepository;

    private MemberEntity memberEntity1;
    private MemberEntity memberEntity2;
    private CoachEntity coachEntity1;

    private String name;
    private String email;

    private String idMessage;
    private String invalidEmail;
    private String emailExistsMessage;
    private String wrongEmail;
    private String noMembers;

    private MemberEntity fakeMember;


    @BeforeEach
    void setup() {
        memberEntity1 = MemberTestData.createSeedMember1();
        memberEntity2 = MemberTestData.createSeedMember2();
        coachEntity1 = CoachTestData.createSeedCoach1();

        name = memberEntity1.getName();
        email = memberEntity1.getEmail();

        idMessage = "Member with an id of: " + 1L + " doesnt exist";
        emailExistsMessage = "Member with an email of: " + memberEntity1.getEmail() + " already exists";
        invalidEmail = "Email cannot be null or empty";
        wrongEmail = "Member with an email of: " + email + " doesnt exist";
        noMembers = "There are currently no members registered";

        fakeMember = new MemberEntity(
                "Ginger Green",
                LocalDate.of(1970, 4, 12),
                "2023-10-10",
                "gingerGreen@gmail.com",
                Roles.ROLE_MEMBER,
               100, 100, 100, 300
        );
    }

    @Test
    void registerNewMember_SuccessfullySavesMemberToDB_IfCredentialsAreValid() {
        when(memberRepository.existsByEmail(fakeMember.getEmail())).thenReturn(false);

        memberService.registerNewMember(fakeMember);

        verify(memberRepository, times(1)).save(fakeMember);
    }

    @Test
    void registerNewMember_ThrowsExceptions_IfCredentialsArentValid() {
        when(memberRepository.existsByEmail(memberEntity1.getEmail())).thenReturn(true);

        var exception = assertThrows(IllegalStateException.class, () -> memberService.registerNewMember(memberEntity1));
        assertEquals(emailExistsMessage, exception.getMessage());

        verify(memberRepository, times(0)).save(memberEntity1);
    }

    @Test
    void registerNewMembers_SuccessfullySavesMembersToDB_IfCredentialsAreValid() {
        when(memberRepository.existsByEmail(memberEntity1.getEmail())).thenReturn(false);
        when(memberRepository.existsByEmail(memberEntity2.getEmail())).thenReturn(false);

        memberService.registerNewMembers(List.of(memberEntity1, memberEntity2));

        verify(memberRepository, times(1)).existsByEmail(memberEntity1.getEmail());
        verify(memberRepository, times(1)).existsByEmail(memberEntity2.getEmail());
        verify(memberRepository, times(1)).saveAll(List.of(memberEntity1, memberEntity2));
    }

    @Test
    void registerNewMembers_ThrowsExceptions_IfCredentialsArentValid() {
        when(memberRepository.existsByEmail(memberEntity1.getEmail())).thenReturn(true);

        var exception = assertThrows(IllegalStateException.class, () ->
                memberService.registerNewMembers(List.of(memberEntity1, memberEntity2)));
        assertEquals(emailExistsMessage, exception.getMessage());

        verify(memberRepository, times(1)).existsByEmail(memberEntity1.getEmail());
        verifyNoMoreInteractions(memberRepository);
    }
}
