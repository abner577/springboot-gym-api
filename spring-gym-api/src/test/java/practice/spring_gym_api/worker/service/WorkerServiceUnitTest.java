package practice.spring_gym_api.worker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import practice.spring_gym_api.dto.WorkerMapper;
import practice.spring_gym_api.dto.request.WorkerRequestDTO;
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

@ExtendWith(MockitoExtension.class)
public class WorkerServiceUnitTest {

    @InjectMocks
    WorkerServiceimpl workerService;

    @Mock
    WorkerRepository workerRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    CoachRepository coachRepository;

    @Mock
    WorkerMapper workerMapper;

    private WorkerEntity workerEntity1;
    private WorkerEntity workerEntity2;
    private CoachEntity coachEntity1;
    private MemberEntity memberEntity1;
    private WorkerEntity fakeWorker;

    private WorkerRequestDTO worker1RequestDTO;
    private WorkerRequestDTO fakeWorkerRequestDTO;

    private String name;
    private String email;
    private String code;

    private String idMessage;
    private String invalidEmail;
    private String emailDoesntExist;
    private String differentMembers;

    @BeforeEach
    void setup() {
        workerEntity1 = WorkerTestData.createSeedWorker1();
        workerEntity2 = WorkerTestData.createSeedWorker2();

        coachEntity1 = CoachTestData.createSeedCoach1();
        memberEntity1 = MemberTestData.createSeedMember1();

        name = workerEntity1.getName();
        email = workerEntity1.getEmail();
        code = workerEntity1.getWorkerCode();
        differentMembers = "Worker with an email of: " + email + " is not the same worker with an id of: " + 1L;

        idMessage = "Worker with an id of: " + 1L + " doesnt exist";
        invalidEmail = "Email cannot be null or empty";
        emailDoesntExist = "Worker with an email of: " + email + " doesnt exist";

        fakeWorker = new WorkerEntity(
               "Andrew Matthew", LocalDate.of(1950, 05, 18),
                Roles.ROLE_MEMBER, "andrew123@gmail.com", "000-999-111"
        );

        worker1RequestDTO = new WorkerRequestDTO(
                name, workerEntity1.getDateOfBirth(), workerEntity1.getRole(),
                email, "Placeholder worker code"
        );

        fakeWorkerRequestDTO = new WorkerRequestDTO(
                fakeWorker.getName(), fakeWorker.getDateOfBirth(), fakeWorker.getRole(),
                fakeWorker.getEmail(), "Placeholder worker code"
        );
    }

    @Test
    void getWorkerById_SuccessfullyReturnsWorkerEntity_WhenIdIsValid() {
        when(workerRepository.findById(1L)).thenReturn(Optional.ofNullable(workerEntity1));

        WorkerEntity workerEntityReturned = workerService.getWorkerById(1L);
        assertTrue(Objects.equals(workerEntity1, workerEntityReturned));

        verify(workerRepository, times(1)).findById(1L);
    }

    @Test
    void getWorkerById_ThrowsException_WhenIdIsInvalid() {
        when(workerRepository.findById(1L)).thenReturn(Optional.empty());

        var exceptipn = assertThrows(NoSuchElementException.class, () -> workerService.getWorkerById(1L));
        assertEquals(idMessage, exceptipn.getMessage());

        verify(workerRepository, times(1)).findById(1L);
    }

    @Test
    void getWorkerByWorkerCode_SuccessfullyReturnsWorkerEntity_WhenCredentialsAreValid() {
        when(workerRepository.findById(1L)).thenReturn(Optional.ofNullable(workerEntity1));
        when(workerRepository.findByWorkerCode(code)).thenReturn(workerEntity1);

        WorkerEntity workerEntityReturned = workerService.getWorkerByWorkerCode(1L,code);
        assertEquals(workerEntity1, workerEntityReturned);

        verify(workerRepository, times(1)).findById(1L);
        verify(workerRepository, times(1)).findByWorkerCode(code);
    }

    @Test
    void getWorkerByWorkerCode_ThrowsException_WhenIdDoesntExist() {
        when(workerRepository.findById(1L)).thenReturn(Optional.empty());

        var exception = assertThrows(NoSuchElementException.class, () -> workerService.getWorkerByWorkerCode(1L, code));
        assertEquals(idMessage, exception.getMessage());

        verify(workerRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(workerRepository);
    }

    @Test
    void getWorkerByWorkerCode_ThrowsException_WhenCodeIsntValid() {
        when(workerRepository.findById(1L)).thenReturn(Optional.ofNullable(workerEntity1));

        var exception = assertThrows(IllegalArgumentException.class, () -> workerService.getWorkerByWorkerCode(1L, ""));
        assertEquals("Code cannot be null or empty", exception.getMessage());

        verify(workerRepository, times(1)).findById(1L);
    }

    @Test
    void getWorkerByWorkerCode_ThrowsException_WhenCodeDoesntExist() {
        when(workerRepository.findById(1L)).thenReturn(Optional.ofNullable(workerEntity1));
        when(workerRepository.findByWorkerCode(code)).thenReturn(null);

        var exception = assertThrows(NoSuchElementException.class, () -> workerService.getWorkerByWorkerCode(1L, code));
        assertEquals("Worker with a worker code of: " + code + " doesnt exist", exception.getMessage());

        verify(workerRepository, times(1)).findById(1L);
        verify(workerRepository, times(1)).findByWorkerCode(code);
    }

    @Test
    void getWorkerByWorkerCode_ThrowsException_WhenCodeAndIdDontBelongToSameMeber() {
        when(workerRepository.findById(1L)).thenReturn(Optional.ofNullable(workerEntity1));
        when(workerRepository.findByWorkerCode(code)).thenReturn(workerEntity2);

        var exception = assertThrows(IllegalStateException.class, () -> workerService.getWorkerByWorkerCode(1L, code));
        assertEquals("Worker with an id of: " + 1L + " is not the same worker with a worker code of: "
                 + code, exception.getMessage());

        verify(workerRepository, times(1)).findById(1L);
        verify(workerRepository, times(1)).findByWorkerCode(code);
    }

    @Test
    void registerNewMember_SuccessfullySavesMember_WhenCredentialsAreValid() {
        when(workerRepository.findByEmail(fakeWorkerRequestDTO.getEmail())).thenReturn(null);
        when(workerMapper.convertToWorkerEntity(fakeWorkerRequestDTO)).thenReturn(fakeWorker);

        workerService.registerNewWorker(fakeWorkerRequestDTO);

        verify(workerRepository, times(1)).findByEmail(fakeWorkerRequestDTO.getEmail());
        verify(workerRepository, times(1)).save(fakeWorker);

    }

    @Test
    void registerNewMember_ThrowsException_WhenCredentialsAreInvalid() {
        when(workerRepository.findByEmail(worker1RequestDTO.getEmail())).thenReturn(workerEntity1);

        var exception = assertThrows(IllegalStateException.class, () -> workerService.registerNewWorker(worker1RequestDTO));
        assertEquals("Worker with an email of: " + worker1RequestDTO.getEmail() + " already exists",
                exception.getMessage());

        verify(workerRepository, times(1)).findByEmail(worker1RequestDTO.getEmail());
        verifyNoMoreInteractions(workerRepository);
    }

    @Test
    void updateWorkerCodeById_SucessfullyUpdatesWorkerCode_WhenCredentialsAreValid() {
        when(workerRepository.findById(1L)).thenReturn(Optional.ofNullable(workerEntity1));
        when(workerRepository.findByEmail(email)).thenReturn(workerEntity1);
        when(workerRepository.findByWorkerCode(fakeWorker.getWorkerCode())).thenReturn(null);

        workerService.updateWorkerCodeById(1L, email, fakeWorker.getWorkerCode());
        assertEquals(workerEntity1.getWorkerCode(), fakeWorker.getWorkerCode());

        verify(workerRepository, times(1)).findById(1L);
        verify(workerRepository, times(1)).findByEmail(email);
        verify(workerRepository, times(1)).findByWorkerCode(fakeWorker.getWorkerCode());
        verify(workerRepository, times(1)).save(workerEntity1);
    }

    @Test
    void updateWorkerCodeById_ThrowsException_WhenIdDoesntExist() {
        when(workerRepository.findById(1L)).thenReturn(Optional.empty());

        var exception = assertThrows(NoSuchElementException.class, () -> workerService.updateWorkerCodeById(1L, email, code));
        assertEquals(idMessage, exception.getMessage());

        verify(workerRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(workerRepository);
    }

    @Test
    void updateWorkerCodeById_ThrowsException_WhenEmailIsInvalid() {
        when(workerRepository.findById(1L)).thenReturn(Optional.ofNullable(workerEntity1));

        var exception = assertThrows(IllegalArgumentException.class, () -> workerService.updateWorkerCodeById(1L, "", code));
        assertEquals(invalidEmail, exception.getMessage());

        verify(workerRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(workerRepository);
    }

    @Test
    void updateWorkerCodeById_ThrowsException_WhenCodeIsInvalid() {
        when(workerRepository.findById(1L)).thenReturn(Optional.ofNullable(workerEntity1));

        var exception = assertThrows(IllegalArgumentException.class, () -> workerService.updateWorkerCodeById(1L, email, ""));
        assertEquals("Worker code cannot be null or empty", exception.getMessage());

        verify(workerRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(workerRepository);
    }

    @Test
    void updateWorkerCodeById_ThrowsException_WhenEmailDoesntExist() {
        when(workerRepository.findById(1L)).thenReturn(Optional.ofNullable(workerEntity1));
        when(workerRepository.findByEmail(email)).thenReturn(null);

        var exception = assertThrows(NoSuchElementException.class, () -> workerService.updateWorkerCodeById(1L, email, code));
        assertEquals(emailDoesntExist, exception.getMessage());

        verify(workerRepository, times(1)).findById(1L);
        verify(workerRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(workerRepository);
    }

    @Test
    void updateWorkerCodeById_ThrowsException_WhenEmailAndIdDontBelongToSameWorker() {
        when(workerRepository.findById(1L)).thenReturn(Optional.ofNullable(workerEntity1));
        when(workerRepository.findByEmail(email)).thenReturn(workerEntity2);

        var exception = assertThrows(IllegalStateException.class, () -> workerService.updateWorkerCodeById(1L, email, code));
        assertEquals(differentMembers, exception.getMessage());

        verify(workerRepository, times(1)).findById(1L);
        verify(workerRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(workerRepository);
    }

    @Test
    void updateWorkerCodeById_ThrowsException_WhenCodeAlreadyExists() {
        when(workerRepository.findById(1L)).thenReturn(Optional.ofNullable(workerEntity1));
        when(workerRepository.findByEmail(email)).thenReturn(workerEntity1);
        when(workerRepository.findByWorkerCode(code)).thenReturn(workerEntity1);

        var exception = assertThrows(IllegalStateException.class, () -> workerService.updateWorkerCodeById(1L, email, code));
        assertEquals("The updated worker code that you are trying to give to: "
                + name + " is already registered under another worker", exception.getMessage());

        verify(workerRepository, times(1)).findById(1L);
        verify(workerRepository, times(1)).findByEmail(email);
        verify(workerRepository, times(1)).findByWorkerCode(code);
        verifyNoMoreInteractions(workerRepository);
    }

    @Test
    void deleteWorkerbyId_SuccessfullyDeletesWorker_WhenIdIsValid() {
        when(workerRepository.findById(1L)).thenReturn(Optional.ofNullable(workerEntity1));

        workerService.deleteWorkerbyId(1L);
        assertFalse(workerRepository.existsByEmail(email));

        verify(workerRepository, times(1)).findById(1L);
        verify(workerRepository, times(1)).delete(workerEntity1);
    }

    @Test
    void deleteWorkerbyId_ThrowsException_WhenIdIsntValid() {
        when(workerRepository.findById(1L)).thenReturn(Optional.empty());

        var exception = assertThrows(NoSuchElementException.class, () -> workerService.deleteWorkerbyId(1L));
        assertEquals(idMessage, exception.getMessage());

        verify(workerRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(workerRepository);
    }

    // error unit tests for updateRole
    @Test
    void updateRoleOfAWorker_ThrowsException_WhenEmailDoesntExist() {
        when(workerRepository.findById(1L)).thenReturn(Optional.ofNullable(workerEntity1));
        when(workerRepository.findByEmail(email)).thenReturn(null);

        var exception = assertThrows(NoSuchElementException.class, () ->
                workerService.updateRoleOfAWorker(1L, email, "ROLE_COACH"));
        assertEquals(emailDoesntExist, exception.getMessage());

        verify(workerRepository, times(1)).findById(1L);
        verify(workerRepository, times(1)).findByEmail(email);
    }

    @Test
    void updateRoleOfAWorker_ThrowsException_WhenEmailAndIdDontBelongToSameWorker() {
        when(workerRepository.findById(1L)).thenReturn(Optional.ofNullable(workerEntity1));
        when(workerRepository.findByEmail(email)).thenReturn(workerEntity2);

        var exception = assertThrows(IllegalStateException.class, () -> workerService.updateRoleOfAWorker(1L, email, "ROLE_COACH"));
        assertEquals(differentMembers, exception.getMessage());

        verify(workerRepository, times(1)).findById(1L);
        verify(workerRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(workerRepository);
    }

    @Test
    void updateRoleOfAWorker_ThrowsException_WhenRoleIsWorker() {
        when(workerRepository.findById(1L)).thenReturn(Optional.ofNullable(workerEntity1));
        when(workerRepository.findByEmail(email)).thenReturn(workerEntity1);

        var exception = assertThrows(IllegalStateException.class, () -> workerService.updateRoleOfAWorker(1L, email, "ROLE_WORKER"));
        assertEquals("Coach: " + name + " already has a role of ROLE_WORKER", exception.getMessage());

        verify(workerRepository, times(1)).findById(1L);
        verify(workerRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(workerRepository);
    }

    @Test
    void updateRoleOfAWorker_ThrowsException_WhenRoleIsInvalid() {
        when(workerRepository.findById(1L)).thenReturn(Optional.ofNullable(workerEntity1));
        when(workerRepository.findByEmail(email)).thenReturn(workerEntity1);

        var exception = assertThrows(IllegalArgumentException.class, () -> workerService.updateRoleOfAWorker(1L, email, "ROLE_ADMIN"));
        assertEquals("Role must be either ROLE_COACH, ROLE_WORKER, or ROLE_MEMBER", exception.getMessage());

        verify(workerRepository, times(1)).findById(1L);
        verify(workerRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(workerRepository);
    }

    // error unit tests for updateWorker

    @Test
    void updateWorkerById_ThrowsException_WhenEmailDoesntExist() {
        when(workerRepository.findById(1L)).thenReturn(Optional.ofNullable(workerEntity1));
        when(workerRepository.findByEmail(email)).thenReturn(null);

        var exception = assertThrows(NoSuchElementException.class, () ->
                workerService.updateWorkerById(1L, email, fakeWorkerRequestDTO));
        assertEquals(emailDoesntExist, exception.getMessage());

        verify(workerRepository, times(1)).findById(1L);
        verify(workerRepository, times(1)).findByEmail(email);
    }

    @Test
    void updateWorkerById_ThrowsException_WhenEmailAndIdDontBelongSameMember() {
        when(workerRepository.findById(1L)).thenReturn(Optional.ofNullable(workerEntity1));
        when(workerRepository.findByEmail(email)).thenReturn(workerEntity2);

        var exception = assertThrows(IllegalStateException.class, () -> workerService.updateWorkerById(1L, email, fakeWorkerRequestDTO));
        assertEquals(differentMembers, exception.getMessage());

        verify(workerRepository, times(1)).findById(1L);
        verify(workerRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(workerRepository);
    }

    @Test
    void updateWorkerById_ThrowsException_WhenEmailAlreadyExists() {
        fakeWorker.setEmail(workerEntity2.getEmail());

        when(workerRepository.findById(1L)).thenReturn(Optional.ofNullable(workerEntity1));
        when(workerRepository.findByEmail(email)).thenReturn(workerEntity1);
        when(workerRepository.findByEmail(fakeWorkerRequestDTO.getEmail())).thenReturn(workerEntity2);

        var exception = assertThrows(IllegalStateException.class, () -> workerService.updateWorkerById(1L, email, fakeWorkerRequestDTO));
        assertEquals("The updated email that you are trying to give to " + name
                + " is already registered under another worker", exception.getMessage());

        verify(workerRepository, times(1)).findById(1L);
        verify(workerRepository, times(1)).findByEmail(email);
        verify(workerRepository, times(1)).findByEmail(fakeWorkerRequestDTO.getEmail());
        verifyNoMoreInteractions(workerRepository);
    }

    @Test
    void updateWorkerById_ThrowsException_WhenWorkerCodeAlreadyExists() {
        fakeWorker.setWorkerCode(workerEntity2.getWorkerCode());

        when(workerRepository.findById(1L)).thenReturn(Optional.ofNullable(workerEntity1));
        when(workerRepository.findByEmail(email)).thenReturn(workerEntity1);
        when(workerRepository.findByWorkerCode(fakeWorkerRequestDTO.getWorkerCode())).thenReturn(workerEntity2);

        var exception = assertThrows(IllegalStateException.class, () -> workerService.updateWorkerById(1L, email, fakeWorkerRequestDTO));
        assertEquals("The updated worker code that you are trying to give to "
                + name + " is already registered under another worker", exception.getMessage());

        verify(workerRepository, times(1)).findById(1L);
        verify(workerRepository, times(1)).findByEmail(email);
        verify(workerRepository, times(1)).findByWorkerCode(fakeWorkerRequestDTO.getWorkerCode());
    }
}
