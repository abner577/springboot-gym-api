package practice.spring_gym_api.coach.controller;


import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import practice.spring_gym_api.controller.CoachController;
import practice.spring_gym_api.dto.CoachDTO;
import practice.spring_gym_api.dto.CoachMapper;
import practice.spring_gym_api.dto.MemberDTO;
import practice.spring_gym_api.dto.MemberMapper;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.security.filter.CoachAuthFilter;
import practice.spring_gym_api.security.filter.ValidRequestFilter;
import practice.spring_gym_api.security.filter.WorkerAuthFilter;
import practice.spring_gym_api.service.CoachService;
import practice.spring_gym_api.testdata.dto.CoachDTOTestData;
import practice.spring_gym_api.testdata.dto.MemberDTOTestData;
import practice.spring_gym_api.testdata.entity.CoachTestData;
import org.junit.jupiter.api.Test;
import practice.spring_gym_api.testdata.entity.MemberTestData;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = CoachController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                CoachAuthFilter.class,
                ValidRequestFilter.class,
                WorkerAuthFilter.class
        })
})
@AutoConfigureMockMvc
public class CoachControllerGETUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CoachService coachService;

    @MockitoBean(name = "coachMapperimpl")
    private CoachMapper coachMapper;

    @MockitoBean
    private MemberMapper memberMapper;


    private CoachEntity coachEntity1;
    private CoachEntity coachEntity2;
    private CoachDTO coachDTO1;
    private CoachDTO coachDTO2;

    @BeforeEach
    void setup(){
        coachEntity1 = CoachTestData.createSeedCoach1();
        coachEntity2 = CoachTestData.createSeedCoach2();
        coachDTO1 = CoachDTOTestData.createSeedDTOCoach1();
        coachDTO2 = CoachDTOTestData.createSeedDTOCoach2();
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"COACH"})
    void getCoachById_ReturnsCoachDTO_WhenCoachExists() throws Exception {
        // Arrange
        when(coachService.getCoachById(1L)).thenReturn(coachEntity1);
        when(coachMapper.convertToCoachDto(coachEntity1)).thenReturn(coachDTO1);

        // Act
        mvc.perform(get("/api/v1/gym-api/coaches/" + 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alex Smith"))
                .andExpect(jsonPath("$.age").value(45))
                .andExpect(jsonPath("$.dateOfBirth").value("1980-04-12"))

                .andExpect(jsonPath("$.email").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.id").doesNotHaveJsonPath());


        // Assert
        verify(coachService, times(1)).getCoachById(1L);
        verifyNoMoreInteractions(coachService);
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"COACH"})
    void getCoachById_ReturnsNotFound_WhenCoachDoesNotExist() throws Exception {
        // Arrange
        Long fakeID = 999L;
        when(coachService.getCoachById(fakeID))
                .thenThrow(new NoSuchElementException("Coach with an id of: " + fakeID + " doesnt exist"));

        // Act
        mvc.perform(get("/api/v1/gym-api/coaches/" + fakeID))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Coach with an id of: " + fakeID + " doesnt exist"));

        // Assert
        verify(coachService, times(1)).getCoachById(fakeID);
        verifyNoMoreInteractions(coachService);
        verify(coachMapper, never()).convertToCoachDto(any());
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"COACH"})
    void getWorkoutPlansByCoachName_ReturnsList_WhenCoachExists() throws Exception{
        // Arrange
        when(coachService.getWorkoutPlansByCoachName(coachDTO1.getName()))
                .thenReturn(List.of("FBEOD", "Upper/Lower"));

        // Act
        mvc.perform(get("/api/v1/gym-api/coaches/" + coachDTO1.getName() + "/plans"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("FBEOD"))
                .andExpect(jsonPath("$[1]").value("Upper/Lower"));

        // Assert
        verify(coachService, times(1)).getWorkoutPlansByCoachName("Alex Smith");
        verifyNoMoreInteractions(coachService);
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"COACH"})
    void getAllAvailableCoaches_ReturnsCoachDTOList_WhenCoachesAvailable() throws Exception {
        // Arrange
        when(coachService.getAllCoachesThatAreAvaliable())
                .thenReturn(List.of(coachEntity1, coachEntity2));
        when(coachMapper.convertToCoachDto(coachEntity1))
                .thenReturn(coachDTO1);
        when(coachMapper.convertToCoachDto(coachEntity2))
                .thenReturn(coachDTO2);

        // Act
        mvc.perform(get("/api/v1/gym-api/coaches/no-clients"))
                .andDo(print())
                .andExpect(jsonPath("$[0]").isNotEmpty())
                .andExpect(jsonPath("$[1]").isNotEmpty())
                .andExpect(jsonPath("$[0].name").value("Alex Smith"))
                .andExpect(jsonPath("$[1].name").value("Maria Gonzalez"))

                .andExpect(jsonPath("$[0].id").doesNotHaveJsonPath())
                .andExpect(jsonPath("$[0].role").doesNotHaveJsonPath())
                .andExpect(jsonPath("$[1].id").doesNotHaveJsonPath())
                .andExpect(jsonPath("$[1].role").doesNotHaveJsonPath());

        // Aseert
        verify(coachService, times(1)).getAllCoachesThatAreAvaliable();
        verifyNoMoreInteractions(coachService);
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"COACH"})
    void getCoachWithHighestClients_ReturnsCoachDTO_WhenCoachesExist() throws Exception{
        // Arrange
        when(coachService.getCoachWithHighestClients()).thenReturn(coachEntity1);
        when(coachMapper.convertToCoachDto(coachEntity1)).thenReturn(coachDTO1);

        // Act
        mvc.perform(get("/api/v1/gym-api/coaches/most-clients"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(coachEntity1.getName()))
                .andExpect(jsonPath("$.age").value(coachEntity1.getAge()))

                .andExpect(jsonPath("$.id").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.role").doesNotHaveJsonPath());

        // Assert
        verify(coachService, times(1)).getCoachWithHighestClients();
        verifyNoMoreInteractions(coachService);
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"COACH"})
    void getCoachWithLowestClients_ReturnsCoachDTO_WhenCoachesExist() throws Exception {
        // Arrange
        when(coachService.getCoachWithLowestClients()).thenReturn(coachEntity2);
        when(coachMapper.convertToCoachDto(coachEntity2)).thenReturn(coachDTO2);

        // Act
        mvc.perform(get("/api/v1/gym-api/coaches/least-clients"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(coachEntity2.getName()))
                .andExpect(jsonPath("$.age").value(coachEntity2.getAge()))

                .andExpect(jsonPath("$.id").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.role").doesNotHaveJsonPath());

        // Assert
        verify(coachService, times(1)).getCoachWithLowestClients();
        verifyNoMoreInteractions(coachService);
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"COACH"})
    void getAllCoachesPageable_ReturnsPagedCoachDTOList_WhenPageRequested() throws Exception {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<CoachEntity> page = new PageImpl<>(List.of(coachEntity1), pageRequest, 2);

        when(coachService.getAllCoachesPageable(pageRequest)).thenReturn(page);
        when(coachMapper.convertToCoachDto(coachEntity1)).thenReturn(coachDTO1);

        // Act
        mvc.perform(get("/api/v1/gym-api/coaches?page=0&size=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Alex Smith"))
                .andExpect(jsonPath("$[0].id").doesNotHaveJsonPath());

        // Assert
        verify(coachService, times(1)).getAllCoachesPageable(pageRequest);
        verifyNoMoreInteractions(coachService);
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"COACH"})
    void getAllClientsByCoachId_ReturnsMemberDTOList_WhenCoachHasClients() throws Exception {
        // Arrange
        MemberEntity memberEntity1 = MemberTestData.createSeedMember1();
        MemberEntity memberEntity3 = MemberTestData.createSeedMember3();
        MemberEntity memberEntity5 = MemberTestData.createSeedMember5();


        MemberDTO memberDTO1 = MemberDTOTestData.createdSeedMemberDTO1();
        MemberDTO memberDTO3 = MemberDTOTestData.createdSeedMemberDTO3();
        MemberDTO memberDTO5 = MemberDTOTestData.createdSeedMemberDTO5();

        when(coachService.getAllClientsByCoachId(1L))
                .thenReturn(coachEntity1.getClients());
        when(memberMapper.convertToMemberDTO(memberEntity1))
                .thenReturn(memberDTO1);
        when(memberMapper.convertToMemberDTO(memberEntity3))
                .thenReturn(memberDTO3);
        when(memberMapper.convertToMemberDTO(memberEntity5))
                .thenReturn(memberDTO5);

        // Act
        mvc.perform(get("/api/v1/gym-api/coaches/1/clients"))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*]name", containsInAnyOrder(
                        memberDTO1.getName(), memberDTO3.getName(), memberDTO5.getName())))


                .andExpect(jsonPath("$[0].id").doesNotHaveJsonPath())
                .andExpect(jsonPath("$[1].id").doesNotHaveJsonPath())
                .andExpect(jsonPath("$[2].id").doesNotHaveJsonPath());


        // Assert
        verify(coachService, times(1)).getAllClientsByCoachId(1L);
        verifyNoMoreInteractions(coachService);

    }

    @Test
    @WithMockUser(username = "testUser", roles = {"COACH"})
    void getCoachByCoachCode_ReturnsCoachDTO_WhenCoachCodeMatches() throws Exception {
        // Arrange
        when(coachService.getCoachByCoachCode(1L, coachEntity1.getCoachCode()))
                .thenReturn(coachEntity1);
        when(coachMapper.convertToCoachDto(coachEntity1)).thenReturn(coachDTO1);

        // Act
        mvc.perform(get("/api/v1/gym-api/coaches/1/verify")
                        .param("code", coachEntity1.getCoachCode()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(coachEntity1.getName()))
                .andExpect(jsonPath("$.id").doesNotHaveJsonPath());

        // Assert
        verify(coachService, times(1)).getCoachByCoachCode(1L, coachEntity1.getCoachCode());
        verifyNoMoreInteractions(coachService);
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"COACH"})
    void getCoachByCoachCode_ReturnsException_WhenIdAndCoachCodeDontBelongToSameCoach() throws Exception {
        // Arrange
        Long id = coachEntity1.getId();
        String coachCode = coachEntity2.getCoachCode();
        when(coachService.getCoachByCoachCode(id, coachCode))
                .thenThrow(new IllegalStateException("Coach with an id of: " + id + " isnt the same coach with a coach code of: " + coachCode));

        // Act
        mvc.perform(get("/api/v1/gym-api/coaches/" + id + "/verify")
                .param("code", coachCode))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Coach with an id of: " + id + " isnt the same coach with a coach code of: " + coachCode));

        // Assert
        verify(coachService, times(1)).getCoachByCoachCode(id, coachCode);
        verifyNoMoreInteractions(coachService);
        verify(coachMapper, never()).convertToCoachDto(any());
    }
}
