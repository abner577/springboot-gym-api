package practice.spring_gym_api.member.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import practice.spring_gym_api.dto.request.MemberRequestDTO;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.entity.WorkerEntity;
import practice.spring_gym_api.entity.enums.Roles;
import practice.spring_gym_api.repository.CoachRepository;
import practice.spring_gym_api.repository.MemberRepository;
import practice.spring_gym_api.repository.WorkerRepository;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CoachRepository coachRepository;

    @Autowired
    private WorkerRepository workerRepository;

    private MemberEntity memberEntity1;
    private MemberEntity memberEntity2;
    private MemberEntity memberEntity3;
    private MemberEntity memberEntity4;
    private MemberEntity memberEntity5;

    private CoachEntity coachEntity1;
    private CoachEntity coachEntity2;
    private WorkerEntity workerEntity1;

    private MemberEntity fakeMember;
    private MemberRequestDTO fakeMemberRequestDTO;

    private Long seedMemberId;
    private String seedMemberEmail;
    private Long workerId;
    private String workerCode;

    private Long coachId;
    private String coachCode;

    @BeforeEach
    void setup() {
        memberEntity1 = memberRepository.findMemberByEmail("johnDoe@gmail.com");
        seedMemberId = memberEntity1.getId();
        seedMemberEmail = memberEntity1.getEmail();

        memberEntity2 = memberRepository.findMemberByEmail("janeSmith@gmail.com");
        memberEntity3 = memberRepository.findMemberByEmail("davidLee@gmail.com");
        memberEntity4 = memberRepository.findMemberByEmail("emelyChen@gmail.com");
        memberEntity5 = memberRepository.findMemberByEmail("carlosRivera@gmail.com");

        coachEntity1 = coachRepository.findByCoachCode("EMP-990X-YTR8");
        coachId = coachEntity1.getId();
        coachCode = coachEntity1.getCoachCode();

        coachEntity2 = coachRepository.findByCoachCode("WKRCODE-4583");

        workerEntity1 = workerRepository.findByWorkerCode("WKR-8372-LKJD");
        workerId = workerEntity1.getId();
        workerCode = workerEntity1.getWorkerCode();

        fakeMember = new MemberEntity(
                "Merab", LocalDate.of(2000, 05, 05), "2023-10-25",
                "example@gmail.com", Roles.ROLE_MEMBER,
                100, 200, 300, 600
        );

        fakeMemberRequestDTO = new MemberRequestDTO(
                memberEntity1.getName(), memberEntity1.getDateOfBirth(), memberEntity1.getMembershipDate(),
                memberEntity1.getEmail(), memberEntity1.getRole(), memberEntity1.getBench(), memberEntity1.getSquat(),
                memberEntity1.getDeadlift(), memberEntity1.getTotal()
        );
    }

    @Test
    @Transactional
    void registerNewMember_SuccessfullyRegisters_WhenCredentialsAreValid() throws Exception {
        mvc.perform(post("/api/v1/gym-api/members")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fakeMemberRequestDTO))
                        .header("x-worker-id", workerId)
                        .header("x-worker-code", workerCode));

        assertTrue(memberRepository.existsByEmail(fakeMemberRequestDTO.getEmail()));
        assertTrue(memberRepository.findMemberByEmail(fakeMemberRequestDTO.getEmail()).getRole().equals(Roles.ROLE_MEMBER));
    }

    @Test
    @Transactional
    void registerNewMember_ThrowsException_WhenEmailAlreadyExists() throws Exception {
        fakeMemberRequestDTO.setEmail(memberEntity1.getEmail());

       var exception = mvc.perform(post("/api/v1/gym-api/members")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fakeMemberRequestDTO))
                        .header("x-worker-id", workerId)
                        .header("x-worker-code", workerCode))
                        .andReturn();

       String responseBody = exception.getResponse().getContentAsString();
       assertTrue(responseBody.contains(
               "Member with an email of: " + memberEntity1.getEmail() + " already exists"
       ));
    }

    @Test
    @Transactional
    void replaceCoach_SucessfullyReplacesCoach_WhenCredentialsAreValid() throws Exception {
        mvc.perform(patch("/api/v1/gym-api/members/" + seedMemberId + "/oldCoach/" +
                coachEntity1.getId() + "/newCoach/" + coachEntity2.getId())
                .with(csrf())
                .header("x-coach-id", coachId)
                .header("x-coach-code", coachCode));
        coachEntity1.getClients().remove(memberEntity1);
        coachEntity2.getClients().add(memberEntity1);

        assertTrue(Objects.equals(memberEntity1.getCoachedBy(), coachEntity2));
        assertFalse(coachEntity1.getClients().contains(memberEntity1));
        assertTrue(coachEntity2.getClients().contains(memberEntity1));
    }

    @Test
    @Transactional
    void replaceCoach_ThrowsException_WhenCredentialsArentValid() throws Exception {
        var exception = mvc.perform
                (patch("/api/v1/gym-api/members/" + seedMemberId
                        + "/oldCoach/20/newCoach/" + coachEntity2.getId())
                .with(csrf())
                .header("x-coach-id", coachId)
                .header("x-coach-code", coachCode))
                .andReturn();

        String responseBody = exception.getResponse().getContentAsString();
        assertTrue(responseBody.contains("Coach with an id of: " + 20L + " doesnt exist"));
    }

    @Test
    @Transactional
    void updateRole_SuccessfullyUpdatesMemberToCoach_WhenCredentialsAreValid() throws Exception {
        mvc.perform(patch("/api/v1/gym-api/members/" + seedMemberId + "/role")
                .with(csrf())
                .header("x-coach-id", coachId)
                .header("x-coach-code", coachCode)
                .param("email", memberEntity1.getEmail())
                .param("role", "ROLE_COACH")
        );
        CoachEntity entityFromRepo = coachRepository.findByEmail(memberEntity1.getEmail());

        assertFalse(memberRepository.existsByEmail(memberEntity1.getEmail()));
        assertTrue(coachRepository.existsByEmail(memberEntity1.getEmail()));

        assertTrue(entityFromRepo.getClients().isEmpty());
        assertTrue(entityFromRepo.getRole().equals(Roles.ROLE_COACH));
    }

    @Test
    @Transactional
    void updateRole_SuccessfullyUpdatesMemberToWorker_WhenCredentialsAreValid() throws Exception {
        mvc.perform(patch("/api/v1/gym-api/members/" + seedMemberId + "/role")
                .with(csrf())
                .header("x-coach-id", coachId)
                .header("x-coach-code", coachCode)
                .param("email", memberEntity1.getEmail())
                .param("role", "ROLE_WORKER")
        );
       WorkerEntity workerFromRepo = workerRepository.findByEmail(memberEntity1.getEmail());

        assertFalse(memberRepository.existsByEmail(memberEntity1.getEmail()));
        assertTrue(workerRepository.existsByEmail(memberEntity1.getEmail()));

        assertTrue(workerFromRepo.getWorkerCode().equals("Placeholder worker code"));
        assertTrue(Objects.equals(workerFromRepo.getRole(), Roles.ROLE_WORKER));
    }

    @Test
    @Transactional
    void updateRole_ThrowsException_WhenRoleIsEqualToMember() throws Exception {
        var exception = mvc.perform(patch("/api/v1/gym-api/members/" + seedMemberId + "/role")
                .with(csrf())
                .header("x-coach-id", coachId)
                .header("x-coach-code", coachCode)
                .param("email", memberEntity1.getEmail())
                .param("role", "ROLE_MEMBER"))
                .andReturn();

        String responseBody = exception.getResponse().getContentAsString();
        assertTrue(responseBody.contains("Member: " + memberEntity1.getName() +
                " already has a role of ROLE_MEMBER"));
    }

    @Test
    @Transactional
    void deleteMembersBelowATotal_SuccessfullyDeletesMembers_WhenTotalIsntNegative() throws Exception {
        mvc.perform(delete("/api/v1/gym-api/members/below/total/10000")
                .with(csrf())
                .header("x-worker-id", workerId)
                .header("x-worker-code", workerCode));

        assertFalse(memberRepository.existsByEmail(memberEntity1.getEmail()));
        assertFalse(memberRepository.existsByEmail(memberEntity2.getEmail()));
    }

    @Test
    @Transactional
    void deleteMembersBelowATotal_ThrowsExcpetion_WhenTotalIsNegative() throws Exception {
        var exception = mvc.perform(delete("/api/v1/gym-api/members/below/total/-10000")
                .with(csrf())
                .header("x-worker-id", workerId)
                .header("x-worker-code", workerCode))
                .andReturn();

        String responseBody = exception.getResponse().getContentAsString();
        assertTrue(responseBody.contains("Total cannot be negative"));
        assertTrue(memberRepository.existsByEmail(memberEntity1.getEmail()));
        assertTrue(memberRepository.existsByEmail(memberEntity2.getEmail()));
    }
}
