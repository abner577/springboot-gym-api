package practice.spring_gym_api.worker.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import practice.spring_gym_api.controller.WorkerController;
import practice.spring_gym_api.dto.WorkerDTO;
import practice.spring_gym_api.dto.WorkerMapper;
import practice.spring_gym_api.entity.WorkerEntity;
import practice.spring_gym_api.security.filter.CoachAuthFilter;
import practice.spring_gym_api.security.filter.ValidRequestFilter;
import practice.spring_gym_api.security.filter.WorkerAuthFilter;
import practice.spring_gym_api.service.WorkerService;
import practice.spring_gym_api.testdata.dto.WorkerDTOTestData;
import practice.spring_gym_api.testdata.entity.WorkerTestData;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = WorkerController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                CoachAuthFilter.class,
                ValidRequestFilter.class,
                WorkerAuthFilter.class
        })
})
@AutoConfigureMockMvc
public class WorkerControllerGETUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private WorkerService workerService;

    @MockitoBean
    private WorkerMapper workerMapper;

    private WorkerEntity workerEntity1;
    private WorkerEntity workerEntity2;
    private WorkerDTO workerDTO1;
    private WorkerDTO workerDTO2;

    @BeforeEach
    void setup() {
        workerEntity1 = WorkerTestData.createSeedWorker1();
        workerEntity2 = WorkerTestData.createSeedWorker2();
        workerDTO1= WorkerDTOTestData.createdSeedWorkerDTO1();
        workerDTO2 = WorkerDTOTestData.createdSeedWorkerDTO2();
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"COACH"})
    void getWorkerByID_SuccessfullyReturnsWorkerDTO_WhenIdIsValid() throws Exception {
        when(workerService.getWorkerById(1L)).thenReturn(workerEntity1);
        when(workerMapper.convertToWorkerDTO(workerEntity1)).thenReturn(workerDTO1);

        mvc.perform(get("/api/v1/gym-api/worker/" + 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(workerDTO1.getName()))
                .andExpect(jsonPath("$.age").value(workerDTO1.getAge()))

                .andExpect(jsonPath("$.role").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.id").doesNotHaveJsonPath());

        verify(workerService, times(1)).getWorkerById(1L);
        verify(workerMapper, times(1)).convertToWorkerDTO(workerEntity1);
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"COACH"})
    void getWorkerByCode_SuccessfullyReturnsWorkerDTO_WhenWorkerCodeIsValid() throws Exception {
        when(workerService.getWorkerByWorkerCode(1L, workerEntity1.getWorkerCode())).thenReturn(workerEntity1);
        when(workerMapper.convertToWorkerDTO(workerEntity1)).thenReturn(workerDTO1);

        mvc.perform(get("/api/v1/gym-api/worker/" + 1L + "/")
                        .param("code", workerEntity1.getWorkerCode()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(workerDTO1.getName()))
                .andExpect(jsonPath("$.age").value(workerDTO1.getAge()))

                .andExpect(jsonPath("$.role").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.id").doesNotHaveJsonPath());

        verify(workerService, times(1)).getWorkerByWorkerCode(1L, workerEntity1.getWorkerCode());
        verify(workerMapper, times(1)).convertToWorkerDTO(workerEntity1);
    }
}
