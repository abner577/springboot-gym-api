package practice.spring_gym_api.coach;


import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import practice.spring_gym_api.controller.CoachController;
import practice.spring_gym_api.dto.CoachDTO;
import practice.spring_gym_api.dto.CoachMapper;
import practice.spring_gym_api.dto.MemberMapper;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.security.filter.CoachAuthFilter;
import practice.spring_gym_api.security.filter.ValidRequestFilter;
import practice.spring_gym_api.security.filter.WorkerAuthFilter;
import practice.spring_gym_api.service.CoachService;
import practice.spring_gym_api.testdata.dto.CoachDTOTestData;
import practice.spring_gym_api.testdata.entity.CoachTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CoachControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CoachService coachService;

    @MockitoBean(name = "coachMapperimpl")
    private CoachMapper coachMapper;

    @MockitoBean
    private MemberMapper memberMapper;

    private CoachDTO coachDTO;

    @BeforeAll
    void setup() {
        CoachEntity coachEntity = CoachTestData.createSeedCoach1();
        CoachDTO coachDTO = CoachDTOTestData.createSeedDTOCoach1();
        when(coachService.getCoachById(1L)).thenReturn(coachEntity);
        when(coachMapper.convertToCoachDto(coachEntity)).thenReturn(coachDTO);
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"COACH"})
    void controllerReturnsDTOAndResponseOK() throws Exception {
        // Arrange

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
}
