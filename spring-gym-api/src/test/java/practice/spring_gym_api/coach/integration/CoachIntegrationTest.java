package practice.spring_gym_api.coach.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.entity.enums.Roles;
import practice.spring_gym_api.repository.CoachRepository;
import practice.spring_gym_api.repository.MemberRepository;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

    @Autowired
    private MemberRepository memberRepository;

    private MemberEntity fakeMember1;
    private MemberEntity fakeMember2;
    private CoachEntity coachEntity;
    private CoachEntity coachEntity2;

    private Long seedCoachId;
    private String seedCoachEmail;

    @BeforeEach
    void setup() {
        coachEntity = coachRepository.findByCoachCode("EMP-990X-YTR8");
        if(coachEntity == null) throw new RuntimeException("Seed coach not found");
        seedCoachId = coachEntity.getId();
        seedCoachEmail = coachEntity.getEmail();

        coachEntity2 = coachRepository.findByCoachCode("WKRCODE-4583");
        if(coachEntity2 == null) throw new RuntimeException("Seed coach not found");


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

    @Transactional
    @Test
    void replaceClientsByIdAndEmail_SuccessfullyReaplcesClientList_WhenIdAndEmailAreValid() throws Exception {
       // Ids of new clients
       List<Long> idsOfUpdatedClients = new ArrayList<>();
       Set<MemberEntity> updatedClients = new HashSet<>();

       for(MemberEntity client : coachEntity2.getClients()) {
           MemberEntity memberEntity = memberRepository.findMemberByEmail(client.getEmail());
           idsOfUpdatedClients.add(memberEntity.getId());
           updatedClients.add(memberEntity);
       }

       // Performing query
        mvc.perform(patch("/api/v1/gym-api/replace/coach/clients/" + seedCoachId)
                        .with(csrf())
                        .queryParam("email", seedCoachEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(idsOfUpdatedClients))
                        .header("x-coach-code", "EMP-990X-YTR8")
                        .header("x-coach-id", seedCoachId));

      coachEntity.getClients().clear();
      for(MemberEntity member : updatedClients) coachEntity.getClients().add(member);

        assertTrue(coachEntity.getClients().size() == 2);
        assertThat(updatedClients).extracting(MemberEntity::getName)
                .containsExactlyInAnyOrder("Emily Chen", "Jane Smith");

        for(MemberEntity member : coachEntity.getClients()){
            assertThat(member.getCoachedBy().getId()).isEqualTo(seedCoachId);
        }
    }

    @Transactional
    @Test
    void replaceClientsByIdAndEmail_ThrowsException_WhenCoachIdDoesntExist() throws Exception {
        List<Long> idsOfUpdatedClients = new ArrayList<>();

        for(MemberEntity client : coachEntity2.getClients()) {
            MemberEntity memberEntity = memberRepository.findMemberByEmail(client.getEmail());
            idsOfUpdatedClients.add(memberEntity.getId());
        }
        var exception = mvc.perform(patch("/api/v1/gym-api/replace/coach/clients/" + 15L)
                .with(csrf())
                .queryParam("email", seedCoachEmail)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(idsOfUpdatedClients))
                .header("x-coach-code", "EMP-990X-YTR8")
                .header("x-coach-id", seedCoachId))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = exception.getResponse().getContentAsString();
        assertTrue(responseBody.contains("Coach with an id of: " + 15L + " doesnt exist"));
    }
}

