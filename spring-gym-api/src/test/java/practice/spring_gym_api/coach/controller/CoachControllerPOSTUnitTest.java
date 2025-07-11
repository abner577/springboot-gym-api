package practice.spring_gym_api.coach.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import practice.spring_gym_api.controller.CoachController;
import practice.spring_gym_api.dto.CoachMapper;
import practice.spring_gym_api.dto.MemberMapper;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.enums.Roles;
import practice.spring_gym_api.security.filter.CoachAuthFilter;
import practice.spring_gym_api.security.filter.ValidRequestFilter;
import practice.spring_gym_api.security.filter.WorkerAuthFilter;
import practice.spring_gym_api.service.CoachService;
import practice.spring_gym_api.testdata.entity.CoachTestData;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
public class CoachControllerPOSTUnitTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    CoachService coachService;

    @MockitoBean(name = "coachMapperimpl")
    CoachMapper coachMapper;

    @MockitoBean
    MemberMapper memberMapper;

    private CoachEntity coachEntity1;
    private CoachEntity coachEntity2;
    private CoachEntity fakeCoachEntity;

    @BeforeEach
    void setup(){
        coachEntity1 = CoachTestData.createSeedCoach1();
        coachEntity2 = CoachTestData.createSeedCoach2();

        fakeCoachEntity = new CoachEntity(
                3L,
                "Ginger Green",
                LocalDate.of(1970, 4, 12),
                Roles.ROLE_COACH,
                "gingerGreen@gmail.com",
                List.of("PPL", "Arnold"),
                "PEM-990X-YTR8"
        );
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"COACH"})
    void registerNewCoach_ThrowsIllegalArgumentException_WhenEmailOfNewCoachAlreadyExists() throws Exception {
        // Arrange
        doThrow(new IllegalArgumentException("Coach with an email of: " + coachEntity1.getEmail() + " already exists"))
                .when(coachService).registerNewCoach(fakeCoachEntity);

        // Act
        System.out.println(objectMapper.writeValueAsString(fakeCoachEntity));
        mvc.perform(post("/api/v1/gym-api/coach")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fakeCoachEntity)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Coach with an email of: " + coachEntity1.getEmail() + " already exists"));

        // Assert
        verify(coachService, times(1)).registerNewCoach(fakeCoachEntity);
        verifyNoMoreInteractions(coachService);
    }
}
