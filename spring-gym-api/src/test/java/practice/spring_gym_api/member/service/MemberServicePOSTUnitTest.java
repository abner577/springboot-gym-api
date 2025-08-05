package practice.spring_gym_api.member.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import practice.spring_gym_api.dto.MemberMapper;
import practice.spring_gym_api.dto.request.MemberRequestDTO;
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

    @Mock
    private MemberMapper memberMapper;

    private MemberEntity memberEntity1;
    private MemberRequestDTO memberEntity1RequestDTO;
    private MemberEntity memberEntity2;
    private CoachEntity coachEntity1;

    private String name;
    private String email;

    private String idMessage;
    private String invalidEmail;
    private String emailExistsMessage;
    private String wrongEmail;
    private String noMembers;

    private MemberRequestDTO fakeMemberRequestDTO;
    private MemberEntity fakeMemberEntity;



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

        fakeMemberRequestDTO = new MemberRequestDTO(
                "Ginger Green",
                LocalDate.of(1970, 4, 12),
                "2023-10-10",
                "gingerGreen@gmail.com",
                Roles.ROLE_MEMBER,
               100, 100, 100, 300
        );

        fakeMemberEntity = new MemberEntity(
                "Ginger Green",
                LocalDate.of(1970, 4, 12),
                "2023-10-10",
                "gingerGreen@gmail.com",
                Roles.ROLE_MEMBER,
                100, 100, 100, 300
        );

        memberEntity1RequestDTO = new MemberRequestDTO(
                name, memberEntity1.getDateOfBirth(), memberEntity1.getMembershipDate(), email,
                memberEntity1.getRole(), memberEntity1.getBench(), memberEntity1.getSquat(),
                memberEntity1.getDeadlift(), memberEntity1.getTotal()
        );
    }

    @Test
    void registerNewMember_SuccessfullySavesMemberToDB_IfCredentialsAreValid() {
        when(memberRepository.existsByEmail(fakeMemberRequestDTO.getEmail())).thenReturn(false);
        when(memberMapper.convertToMemberEntity(fakeMemberRequestDTO)).thenReturn(fakeMemberEntity);

        memberService.registerNewMember(fakeMemberRequestDTO);

        verify(memberRepository, times(1)).save(fakeMemberEntity);
    }

    @Test
    void registerNewMember_ThrowsExceptions_IfCredentialsArentValid() {
        when(memberRepository.existsByEmail(memberEntity1RequestDTO.getEmail())).thenReturn(true);

        var exception = assertThrows(IllegalStateException.class, () -> memberService.registerNewMember(memberEntity1RequestDTO));
        assertEquals(emailExistsMessage, exception.getMessage());

        verify(memberRepository, times(0)).save(memberEntity1);
    }

    @Test
    void registerNewMembers_SuccessfullySavesMembersToDB_IfCredentialsAreValid() {
        when(memberRepository.existsByEmail(memberEntity1RequestDTO.getEmail())).thenReturn(false);
        when(memberMapper.convertToMemberEntity(memberEntity1RequestDTO)).thenReturn(memberEntity1);

        memberService.registerNewMembers(List.of(memberEntity1RequestDTO));

        verify(memberRepository, times(1)).existsByEmail(memberEntity1RequestDTO.getEmail());
        verify(memberRepository, times(1)).saveAll(List.of(memberEntity1));
    }

    @Test
    void registerNewMembers_ThrowsExceptions_IfCredentialsArentValid() {
        when(memberRepository.existsByEmail(memberEntity1RequestDTO.getEmail())).thenReturn(true);

        var exception = assertThrows(IllegalStateException.class, () ->
                memberService.registerNewMembers(List.of(memberEntity1RequestDTO)));
        assertEquals(emailExistsMessage, exception.getMessage());

        verify(memberRepository, times(1)).existsByEmail(memberEntity1RequestDTO.getEmail());
        verifyNoMoreInteractions(memberRepository);
    }
}
