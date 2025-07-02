package practice.spring_gym_api.coach;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.repository.CoachRepository;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CoachIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CoachRepository coachRepository;

    private Long seedCoachId;

    @BeforeAll
    void setup() {
        CoachEntity coachEntity = coachRepository.findByCoachCode("EMP-990X-YTR8");
        if(coachEntity == null) throw new RuntimeException("Seed coach not found");
        seedCoachId = coachEntity.getId();
    }

    @Test
    void testGetCoachByIdReturnsExpectedCoach() throws Exception {
        mvc.perform(get("/api/v1/gym-api/coach/id/" + seedCoachId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alex Smith"))
                .andExpect(jsonPath("$.age").value(45))
                .andExpect(jsonPath("$.dateOfBirth").value("1980-04-12"))

                .andExpect(jsonPath("$.email").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.id").doesNotHaveJsonPath());
    }
}

