package practice.spring_gym_api.worker.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import practice.spring_gym_api.dto.request.WorkerRequestDTO;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.entity.WorkerEntity;
import practice.spring_gym_api.entity.enums.Roles;
import practice.spring_gym_api.repository.CoachRepository;
import practice.spring_gym_api.repository.MemberRepository;
import practice.spring_gym_api.repository.WorkerRepository;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class WorkerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CoachRepository coachRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private WorkerRepository workerRepository;

    private WorkerEntity workerEntity1;
    private WorkerEntity workerEntity2;
    private WorkerRequestDTO workerEntity1RequestDTO;

    private CoachEntity coachEntity1;
    private MemberEntity memberEntity1;

    private WorkerEntity fakeWorker;
    private WorkerRequestDTO fakeWorkerRequestDTO;

    private Long seedWorkerID;
    private String seedWorkerEmail;
    private String seedWorkerCode;

    private Long coachId;
    private String coachCode;

    @BeforeEach
    void setup() {
        workerEntity1 = workerRepository.findByWorkerCode("WKR-8372-LKJD");
        seedWorkerID = workerEntity1.getId();
        seedWorkerEmail = workerEntity1.getEmail();
        seedWorkerCode = workerEntity1.getWorkerCode();

        coachEntity1 = coachRepository.findByCoachCode("EMP-990X-YTR8");
        coachId = coachEntity1.getId();
        coachCode = coachEntity1.getCoachCode();

        memberEntity1 = memberRepository.findMemberByEmail("johnDoe@gmail.com");

        fakeWorker = new WorkerEntity(
                "Andrew Matthew", LocalDate.of(1950, 05, 18),
                Roles.ROLE_MEMBER, "andrew123@gmail.com", "000-999-111"
        );

        workerEntity2 = workerRepository.findByWorkerCode("WRK2024-AZ19");

        workerEntity1RequestDTO = new WorkerRequestDTO(
                workerEntity1.getName(), workerEntity1.getDateOfBirth(), workerEntity1.getRole(),
                workerEntity1.getEmail(), "Placeholder worker code"
        );

        fakeWorkerRequestDTO = new WorkerRequestDTO(
                fakeWorker.getName(), fakeWorker.getDateOfBirth(), fakeWorker.getRole(),
                fakeWorker.getEmail(), "Placeholder worker code"
        );
    }

    @Test
    @Transactional
    void updateRoleOfAWorker_SuccessfullyUpdatesRoleOfAWorker_WhenCredentialsAreValid() throws Exception {
        mvc.perform(patch("/api/v1/gym-api/workers/" + seedWorkerID + "/role")
                        .with(csrf())
                .param("email", seedWorkerEmail)
                .param("role", "ROLE_COACH")
                .header("x-coach-id", coachId)
                .header("x-coach-code", coachCode))
                .andDo(print());

        CoachEntity newCoachEntity = coachRepository.findByEmail(workerEntity1.getEmail());

        assertFalse(workerRepository.existsByEmail(workerEntity1.getEmail()));
        assertTrue(newCoachEntity != null);
        assertEquals(newCoachEntity.getRole(), Roles.ROLE_COACH);
    }

    @Test
    @Transactional
    void updateRoleOfAWorker_ThrowsException_WhenRoleEqualsWorker() throws Exception {
        var exception = mvc.perform(patch("/api/v1/gym-api/workers/" + seedWorkerID + "/role")
                        .with(csrf())
                        .param("email", seedWorkerEmail)
                        .param("role", "ROLE_WORKER")
                        .header("x-coach-id", coachId)
                        .header("x-coach-code", coachCode))
                .andDo(print())
                .andReturn();

        String responseBody = exception.getResponse().getContentAsString();
        assertTrue(responseBody.contains("Coach: " + workerEntity1.getName() + " already has a role of ROLE_WORKER"));
    }

    @Test
    @Transactional
    void updateWorkerCodeById_SuccessfullyUpdatesWorkerCode_WhenCredentialsAreValid() throws Exception {
        mvc.perform(patch("/api/v1/gym-api/workers/" + seedWorkerID + "/code")
                        .with(csrf())
                        .param("email", seedWorkerEmail)
                        .param("code", fakeWorker.getWorkerCode())
                        .header("x-coach-id", coachId)
                        .header("x-coach-code", coachCode))
                .andDo(print());

        WorkerEntity updatedWorker = workerRepository.findByEmail(seedWorkerEmail);
        assertEquals(fakeWorker.getWorkerCode(), updatedWorker.getWorkerCode());
    }

    @Test
    @Transactional
    void updateWorkerById_SuccessfullyUpdatesWorker_WhenCredentialsAreValid() throws Exception {
        mvc.perform(put("/api/v1/gym-api/workers/" + seedWorkerID)
                        .with(csrf())
                        .param("email", seedWorkerEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fakeWorker))
                        .header("x-coach-id", coachId)
                        .header("x-coach-code", coachCode))
                .andDo(print());

        assertEquals(fakeWorker, workerEntity1);
        assertEquals(fakeWorker.getName(), workerEntity1.getName());
    }

    @Test
    @Transactional
    void updateWorkerById_ThrowsException_WhenCredentialsArentValid() throws Exception {
        fakeWorker.setEmail(workerEntity2.getEmail());
        var exception = mvc.perform(put("/api/v1/gym-api/workers/" + seedWorkerID)
                        .with(csrf())
                        .param("email", seedWorkerEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fakeWorker))
                        .header("x-coach-id", coachId)
                        .header("x-coach-code", coachCode))
                .andDo(print())
                .andReturn();

        String responseBody = exception.getResponse().getContentAsString();
        assertTrue(responseBody.contains("The updated email that you are trying to give to " + workerEntity1.getName() +
                " is already registered under another worker"));
        assertTrue(workerEntity1.getEmail() != fakeWorker.getEmail());
    }

    @Test
    @Transactional
    void registerNewWorker_SuccessfullySavesNewWorker_WHenCredentialsAreValid() throws Exception {
        mvc.perform(post("/api/v1/gym-api/workers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fakeWorkerRequestDTO))
                        .header("x-worker-id", seedWorkerID)
                        .header("x-worker-code", seedWorkerCode))
                .andDo(print());

        WorkerEntity newWorker = workerRepository.findByEmail(fakeWorkerRequestDTO.getEmail());
        assertTrue(newWorker != null);
    }

    @Test
    @Transactional
    void registerNewWorker_ThrowsException_WHenCredentialsArentValid() throws Exception {
        fakeWorkerRequestDTO.setEmail(workerEntity1.getEmail());
        var exception = mvc.perform(post("/api/v1/gym-api/workers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fakeWorkerRequestDTO))
                        .header("x-worker-id", seedWorkerID)
                        .header("x-worker-code", seedWorkerCode))
                .andDo(print())
                .andReturn();

        String responseBody = exception.getResponse().getContentAsString();
        assertTrue(responseBody.contains("Worker with an email of: " + seedWorkerEmail + " already exists"));

        WorkerEntity newWorker = workerRepository.findByWorkerCode("Placeholder worker code");
        assertTrue(newWorker == null);
    }

}
