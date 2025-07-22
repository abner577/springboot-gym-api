package practice.spring_gym_api.member.controller;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import practice.spring_gym_api.controller.MemberController;
import practice.spring_gym_api.dto.MemberDTO;
import practice.spring_gym_api.dto.MemberMapper;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.security.filter.CoachAuthFilter;
import practice.spring_gym_api.security.filter.ValidRequestFilter;
import practice.spring_gym_api.security.filter.WorkerAuthFilter;
import practice.spring_gym_api.service.MemberService;
import practice.spring_gym_api.testdata.dto.MemberDTOTestData;
import practice.spring_gym_api.testdata.entity.CoachTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import practice.spring_gym_api.testdata.entity.MemberTestData;

import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = MemberController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                CoachAuthFilter.class,
                ValidRequestFilter.class,
                WorkerAuthFilter.class
        })
})
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MemberControllerGETUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private MemberService memberService;

    @MockitoBean("memberMapperimpl")
    private MemberMapper memberMapper;

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
    @WithMockUser(username = "testUser", roles = {"COACH"})
    void getMemberById_SuccessfullyReturnsMemberDTO_WhenIdIsValid() throws Exception {
        when(memberService.getMemberById(1L)).thenReturn(memberEntity1);
        when(memberMapper.convertToMemberDTO(memberEntity1)).thenReturn(memberDTO1);

        mvc.perform(get("/api/v1/gym-api/member/" + 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(memberEntity1.getName()))
                .andExpect(jsonPath("$.age").value(memberEntity1.getAge()))

                .andExpect(jsonPath("$.email").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.id").doesNotHaveJsonPath());

        verify(memberService, times(1)).getMemberById(1L);
        verify(memberMapper, times(1)).convertToMemberDTO(memberEntity1);
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"COACH"})
    void getMemberWithHighestBench_ReturnsMemberWithHighestBench_WhenMembersAreAvaliable() throws Exception {
        when(memberService.getMemberByHighestBench()).thenReturn(memberEntity1);
        when(memberMapper.convertToMemberDTO(memberEntity1)).thenReturn(memberDTO1);

        mvc.perform(get("/api/v1/gym-api/member/highest/bench"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(memberEntity1.getName()))
                .andExpect(jsonPath(".age").value(memberEntity1.getAge()))

                .andExpect(jsonPath("$.id").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.email").doesNotHaveJsonPath());

        verify(memberService, times(1)).getMemberByHighestBench();
        verify(memberMapper, times(1)).convertToMemberDTO(memberEntity1);
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"COACH"})
    void getMemberWithHighestSquat_ReturnsMemberWithHighestSquat_WhenMembersAreAvaliable() throws Exception {
        when(memberService.getMemberByHighestSquat()).thenReturn(memberEntity1);
        when(memberMapper.convertToMemberDTO(memberEntity1)).thenReturn(memberDTO1);

        mvc.perform(get("/api/v1/gym-api/member/highest/squat"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(memberEntity1.getName()))
                .andExpect(jsonPath(".age").value(memberEntity1.getAge()))

                .andExpect(jsonPath("$.id").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.email").doesNotHaveJsonPath());

        verify(memberService, times(1)).getMemberByHighestSquat();
        verify(memberMapper, times(1)).convertToMemberDTO(memberEntity1);
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"COACH"})
    void getMemberWithHighestDeadlift_ReturnsMemberWithHighestDeadlift_WhenMembersAreAvaliable() throws Exception {
        when(memberService.getMemberByHighestDeadlift()).thenReturn(memberEntity1);
        when(memberMapper.convertToMemberDTO(memberEntity1)).thenReturn(memberDTO1);

        mvc.perform(get("/api/v1/gym-api/member/highest/deadlift"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(memberEntity1.getName()))
                .andExpect(jsonPath(".age").value(memberEntity1.getAge()))

                .andExpect(jsonPath("$.id").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.email").doesNotHaveJsonPath());

        verify(memberService, times(1)).getMemberByHighestDeadlift();
        verify(memberMapper, times(1)).convertToMemberDTO(memberEntity1);
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"COACH"})
    void getMemberWithHighestTotal_ReturnsMemberWithHighestTotal_WhenMembersAreAvaliable() throws Exception {
        when(memberService.getMemberByHighestTotal()).thenReturn(memberEntity1);
        when(memberMapper.convertToMemberDTO(memberEntity1)).thenReturn(memberDTO1);

        mvc.perform(get("/api/v1/gym-api/member/highest/total"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(memberEntity1.getName()))
                .andExpect(jsonPath(".age").value(memberEntity1.getAge()))

                .andExpect(jsonPath("$.id").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.email").doesNotHaveJsonPath());

        verify(memberService, times(1)).getMemberByHighestTotal();
        verify(memberMapper, times(1)).convertToMemberDTO(memberEntity1);
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"COACH"})
    void getAllMembersWithAGreaterTotalThan_ReturnsMembersWithAGreaterTotalThanX_WhenMembersAreAvaliable() throws Exception {
        when(memberService.getAllMembersAboveATotal(1000)).thenReturn(List.of(memberEntity1, memberEntity2));
        when(memberMapper.convertToMemberDTO(memberEntity1)).thenReturn(memberDTO1);
        when(memberMapper.convertToMemberDTO(memberEntity2)).thenReturn(memberDTO2);

        mvc.perform(get("/api/v1/gym-api/members/above/total/" + 1000))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(memberEntity1.getName()))
                .andExpect(jsonPath("[0].age").value(memberEntity1.getAge()))
                .andExpect(jsonPath("$[1].name").value(memberEntity2.getName()))
                .andExpect(jsonPath("[1].age").value(memberEntity2.getAge()))

                .andExpect(jsonPath("$[0].id").doesNotHaveJsonPath())
                .andExpect(jsonPath("$[0].email").doesNotHaveJsonPath())
                .andExpect(jsonPath("$[1].id").doesNotHaveJsonPath())
                .andExpect(jsonPath("$[1].email").doesNotHaveJsonPath());

        verify(memberService, times(1)).getAllMembersAboveATotal(1000);
        verify(memberMapper, times(1)).convertToMemberDTO(memberEntity1);
        verify(memberMapper, times(1)).convertToMemberDTO(memberEntity2);
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"COACH"})
    void getAllAvaliableMembers_ReturnsAllMembersWithoutACoach_WhenThereAreAvaliableMembers() throws Exception {
        when(memberService.getAllAvaliableMembers()).thenReturn(List.of(memberEntity1, memberEntity2));
        when(memberMapper.convertToMemberDTO(memberEntity1)).thenReturn(memberDTO1);
        when(memberMapper.convertToMemberDTO(memberEntity2)).thenReturn(memberDTO2);

        mvc.perform(get("/api/v1/gym-api/available/members"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(memberEntity1.getName()))
                .andExpect(jsonPath("[0].age").value(memberEntity1.getAge()))
                .andExpect(jsonPath("$[1].name").value(memberEntity2.getName()))
                .andExpect(jsonPath("[1].age").value(memberEntity2.getAge()))

                .andExpect(jsonPath("$[0].id").doesNotHaveJsonPath())
                .andExpect(jsonPath("$[0].email").doesNotHaveJsonPath())
                .andExpect(jsonPath("$[1].id").doesNotHaveJsonPath())
                .andExpect(jsonPath("$[1].email").doesNotHaveJsonPath());

        verify(memberService, times(1)).getAllAvaliableMembers();
        verify(memberMapper, times(1)).convertToMemberDTO(memberEntity1);
        verify(memberMapper, times(1)).convertToMemberDTO(memberEntity2);
    }
}
