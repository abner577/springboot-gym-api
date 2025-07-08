package practice.spring_gym_api.coach.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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

@WebMvcTest(controllers = CoachController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                CoachAuthFilter.class,
                ValidRequestFilter.class,
                WorkerAuthFilter.class
        })
})
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CoachControllerPOSTUnitTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    CoachService coachService;

    @MockitoBean(name = "coachMapperimpl")
    CoachMapper coachMapper;

    @MockitoBean
    MemberMapper memberMapper;

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


}
