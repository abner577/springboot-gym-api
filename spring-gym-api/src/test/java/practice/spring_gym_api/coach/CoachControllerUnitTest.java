package practice.spring_gym_api.coach;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import practice.spring_gym_api.controller.CoachController;
import practice.spring_gym_api.dto.CoachDTO;
import practice.spring_gym_api.dto.CoachMapper;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.service.CoachService;
import practice.spring_gym_api.testdata.CoachTestData;

@WebMvcTest(CoachController.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CoachControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CoachService coachService;

    private CoachDTO coachDTO;
    private CoachMapper coachMapper;

    @BeforeAll
    void setup() {
        CoachEntity coachEntity = CoachTestData.createSeedCoach1();
        CoachDTO coachDTO = coachMapper.convertToCoachDto(coachEntity);
    }

    @Test
    void controllerReturnsDTOAndResponseOK() throws Exception {

    }
}
