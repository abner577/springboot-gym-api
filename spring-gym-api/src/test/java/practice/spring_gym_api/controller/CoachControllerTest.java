package practice.spring_gym_api.controller;

import controller.CoachController;
import dto.CoachDTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import dto.CoachMapper;
import entity.CoachEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import practice.spring_gym_api.testdata.CoachTestData;
import service.CoachService;

import static org.mockito.Mockito.when;

@WebMvcTest(CoachController.class)
public class CoachControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private CoachMapper coachMapper;

    @MockitoBean
    private CoachService coachService;

    @Test
    void testGetCoachById_returnsProperCoachDTO() throws Exception {
        CoachEntity mockCoach = CoachTestData.createSeedCoach1();
        when(coachService.getCoachById(1L)).thenReturn(mockCoach);

        mockMvc.perform(get("/coach/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alex Smith"))
                .andExpect(jsonPath("$.age").value(45));
    }
}
