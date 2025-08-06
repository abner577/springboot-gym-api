package practice.spring_gym_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.spring_gym_api.dto.WorkerDTO;
import practice.spring_gym_api.dto.WorkerMapper;
import practice.spring_gym_api.dto.request.WorkerRequestDTO;
import practice.spring_gym_api.entity.WorkerEntity;
import practice.spring_gym_api.service.WorkerService;

@RestController
@RequestMapping(path = "api/v1/gym-api")
public class WorkerController {

    private final WorkerService workerService;
    private final WorkerMapper workerMapper;

    public WorkerController(WorkerService workerService, WorkerMapper workerMapper) {
        this.workerService = workerService;
        this.workerMapper= workerMapper;
    }

    @Operation(summary = "Retrieves a worker by their ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Worker successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Worker not found")
    })
    @GetMapping(path = "workers/{worker_id}")
    public WorkerDTO getWorkerByID(@PathVariable("worker_id") Long id) {
        WorkerEntity workerEntity = workerService.getWorkerById(id);
        return workerMapper.convertToWorkerDTO(workerEntity);
    }

    @Operation(summary = "Retrieves a worker by ID and code for verification")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Worker successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid worker code"),
            @ApiResponse(responseCode = "404", description = "Worker not found")
    })
    @GetMapping(path = "workers/{worker_id}/code")
    public WorkerDTO getWorkerByCode(
            @PathVariable("worker_id") Long id,
            @RequestParam String code
    ) {
        WorkerEntity workerEntity = workerService.getWorkerByWorkerCode(id, code);
        return workerMapper.convertToWorkerDTO(workerEntity);
    }

    @Operation(summary = "Registers a new worker")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Worker successfully registered"),
            @ApiResponse(responseCode = "400", description = "Invalid worker data")
    })
    @PostMapping(path = "workers")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Must put 'Placeholder worker code' for workerCode field")
    public void registerNewWorker(@Valid @RequestBody WorkerRequestDTO workerRequestDTO) {
        workerService.registerNewWorker(workerRequestDTO);
    }

    @Operation(summary = "Updates the role of a worker by ID and email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Worker role successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "404", description = "Worker not found")
    })
    @PatchMapping(path = "workers/{worker_id}/role")
    public void updateRoleOfAWorker(
            @PathVariable("worker_id") Long id,
            @RequestParam String email,
            @RequestParam String role
    ) {
        workerService.updateRoleOfAWorker(id, email, role);
    }

    @Operation(summary = "Updates the worker code of a specific worker by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Worker code successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "404", description = "Worker not found")
    })
    @PatchMapping(path = "workers/{worker_id}/code")
    public void updateWorkerCodeById(
            @PathVariable("worker_id") Long id,
            @RequestParam String email,
            @RequestParam String code
    ) {
        workerService.updateWorkerCodeById(id, email, code);
    }

    @Operation(summary = "Updates a worker's complete details using ID and email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Worker successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid update data"),
            @ApiResponse(responseCode = "404", description = "Worker not found")
    })
    @PutMapping(path = "workers/{worker_id}")
    public void updatedWorkerById(
            @PathVariable("worker_id") Long id,
            @RequestParam String email,
            @Valid @RequestBody WorkerRequestDTO workerRequestDTO
    ) {
        workerService.updateWorkerById(id, email, workerRequestDTO);
    }

    @Operation(summary = "Deletes a worker from the system by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Worker successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Worker not found")
    })
    @DeleteMapping(path = "workers/{worker_id}")
    public void deleteWorkerByID(@PathVariable("worker_id") Long id) {
        workerService.deleteWorkerbyId(id);
    }

}
