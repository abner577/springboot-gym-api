package practice.spring_gym_api.coach.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.entity.enums.Roles;
import practice.spring_gym_api.repository.CoachRepository;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
public class CoachIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CoachRepository coachRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MemberEntity fakeMember1;
    private MemberEntity fakeMember2;

    private Long seedCoachId;
    private String seedCoachEmail;

    @BeforeEach
    void setup() {
        CoachEntity coachEntity = coachRepository.findByCoachCode("EMP-990X-YTR8");
        if(coachEntity == null) throw new RuntimeException("Seed coach not found");
        seedCoachId = coachEntity.getId();
        seedCoachEmail = coachEntity.getEmail();

        fakeMember1 = new MemberEntity(
                "Alice Johnson",
                LocalDate.of(1995, 6, 15),
                "2023-01-10",
                "alice.johnson@example.com",
                Roles.ROLE_MEMBER,
                150,
                200,
                250,
                600
        );
        fakeMember2 = new MemberEntity(
                "Bob Smith",
                LocalDate.of(1990, 11, 23),
                "2022-09-05",
                "bob.smith@example.com",
                Roles.ROLE_MEMBER,
                180,
                220,
                270,
                670
        );
    }

    @Test
    void getCoachById_ReturnsExpectedCoach_WhenIdIsValid() throws Exception {
        mvc.perform(get("/api/v1/gym-api/coach/id/" + seedCoachId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alex Smith"))
                .andExpect(jsonPath("$.age").value(45))
                .andExpect(jsonPath("$.dateOfBirth").value("1980-04-12"))

                .andExpect(jsonPath("$.email").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.id").doesNotHaveJsonPath());
    }

    @Test
    void replaceClientsByIdAndEmail_SuccessfullyReaplcesClientList_WhenIdAndEmailAreValid() throws Exception {
        mvc.perform(patch("/api/v1/gym-api/replace/coach/clients/" + seedCoachId)
                        .with(csrf())
                        .queryParam("email", seedCoachEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Set.of(fakeMember1, fakeMember2)))
                        .header("x-coach-code", "EMP-990X-YTR8")
                        .header("x-coach-id", seedCoachId))
                .andDo(print());

        CoachEntity updatedCoach = coachRepository.findById(seedCoachId)
                .orElseThrow(() -> new NoSuchElementException
                        ("Coach with an id of: " + seedCoachId + " doesnt exist"));
        Set<MemberEntity> updatedClients = updatedCoach.getClients();

        assertTrue(updatedCoach.getClients().size() == 2);
        assertThat(updatedClients).extracting(MemberEntity::getName)
                .containsExactlyInAnyOrder("Alice Johnson", "Bob Smith");

        for(MemberEntity member : updatedCoach.getClients()){
            assertThat(member.getCoachedBy().getId()).isEqualTo(seedCoachId);
        }
    }
}

