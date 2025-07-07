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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.server.ResponseStatusException;
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
import org.junit.jupiter.api.TestInstance;
import practice.spring_gym_api.testdata.entity.MemberTestData;

import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
    void controllerReturnsDTOAndResponseOK() throws Exception {
        // Arrange
        when(coachService.getCoachById(1L)).thenReturn(coachEntity1);
        when(coachMapper.convertToCoachDto(coachEntity1)).thenReturn(coachDTO1);

        // Act
        mvc.perform(get("/api/v1/gym-api/coach/id/" + 1L))
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
    void controllerReturnsWorkoutPlansByName() throws Exception{
        // Arrange
        when(coachService.getWorkoutPlansByCoachName(coachDTO1.getName()))
                .thenReturn(List.of("FBEOD", "Upper/Lower"));

        // Act
        mvc.perform(get("/api/v1/gym-api/coach/name/" + coachDTO1.getName()))
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
    void controllerReturnsListOfAvaliableCoachDTOs() throws Exception {
        // Arrange
        when(coachService.getAllCoachesThatAreAvaliable())
                .thenReturn(List.of(coachEntity1, coachEntity2));
        when(coachMapper.convertToCoachDto(coachEntity1))
                .thenReturn(coachDTO1);
        when(coachMapper.convertToCoachDto(coachEntity2))
                .thenReturn(coachDTO2);

        // Act
        mvc.perform(get("/api/v1/gym-api/avaliable/coaches"))
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
    void controllerReturnsCoachWithHighestAmountOfClients() throws Exception{
        // Arrange
        when(coachService.getCoachWithHighestClients()).thenReturn(coachEntity1);
        when(coachMapper.convertToCoachDto(coachEntity1)).thenReturn(coachDTO1);

        // Act
        mvc.perform(get("/api/v1/gym-api/best/coach"))
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
    void controllerReturnsCoachWithLowestAmountOfClients() throws Exception {
        // Arrange
        when(coachService.getCoachWithLowestClients()).thenReturn(coachEntity2);
        when(coachMapper.convertToCoachDto(coachEntity2)).thenReturn(coachDTO2);

        // Act
        mvc.perform(get("/api/v1/gym-api/worst/coach"))
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
    void controllerReturnsFirstPageWithOneCoach() throws Exception {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<CoachEntity> page = new PageImpl<>(List.of(coachEntity1), pageRequest, 2);

        when(coachService.getAllCoachesPageable(pageRequest)).thenReturn(page)
                .thenReturn(page);
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
    void controllerReturnsAllClientsOfACoach() throws Exception {
        // Arrange
        when(coachService.getAllClientsByCoachId(1L))
                .thenThrow(new IllegalStateException("Coach does not have any clients to access"));

        // Act
        mvc.perform(get("/api/v1/gym-api/clients/of/" + 1L))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Coach does not have any clients to access"))
                .andExpect(jsonPath("$.error").value("Bad Request"));

        // Assert
        verify(coachService, times(1)).getAllClientsByCoachId(1L);
        verifyNoMoreInteractions(coachService);
    }
}
