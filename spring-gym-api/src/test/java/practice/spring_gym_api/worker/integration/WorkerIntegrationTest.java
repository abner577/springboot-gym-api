package practice.spring_gym_api.worker.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import practice.spring_gym_api.dto.WorkerMapper;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.entity.WorkerEntity;
import practice.spring_gym_api.entity.enums.Roles;
import practice.spring_gym_api.repository.CoachRepository;
import practice.spring_gym_api.repository.MemberRepository;
import practice.spring_gym_api.repository.WorkerRepository;
import practice.spring_gym_api.service.impl.WorkerServiceimpl;
import practice.spring_gym_api.testdata.entity.CoachTestData;
import practice.spring_gym_api.testdata.entity.MemberTestData;
import practice.spring_gym_api.testdata.entity.WorkerTestData;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
    private CoachEntity coachEntity1;
    private MemberEntity memberEntity1;

    private WorkerEntity fakeWorker;

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
    }

    @Test
    @Transactional
    void updateRoleOfAWorker_SuccessfullyUpdatesRoleOfAWorker_WhenCredentialsAreValid() throws Exception {
        mvc.perform(patch("/api/v1/gym-api/update/worker/role/" + seedWorkerID)
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
        var exception = mvc.perform(patch("/api/v1/gym-api/update/worker/role/" + seedWorkerID)
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
    void updateWorkerCodeById_SuccessfullyUpdatesMember_WhenCredentialsAreValid() throws Exception {
        mvc.perform(patch("/api/v1/gym-api/update/worker/code/" + seedWorkerID)
                        .with(csrf())
                        .param("email", seedWorkerEmail)
                        .param("code", fakeWorker.getWorkerCode())
                        .header("x-coach-id", coachId)
                        .header("x-coach-code", coachCode))
                .andDo(print());

        WorkerEntity updatedWorker = workerRepository.findByEmail(seedWorkerEmail);
        assertEquals(fakeWorker.getWorkerCode(), updatedWorker.getWorkerCode());

    }

}
