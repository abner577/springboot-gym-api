package practice.spring_gym_api.controller;

import controller.CoachController;
import entity.CoachEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import practice.spring_gym_api.testdata.CoachTestData;
import service.CoachService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CoachController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CoachControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CoachService coachService;

    @Test
    void testGetCoachById_ReturnsProperCoachDTO() throws Exception {
        CoachEntity mockCoachEntity = CoachTestData.createSeedCoach1();
        when(coachService.getCoachById(1L)).thenReturn(mockCoachEntity);

        mockMvc.perform(get("/api/v1/gym-api/coach/1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(mockCoachEntity.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(mockCoachEntity.getAge()));
    }
}
