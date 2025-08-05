package practice.spring_gym_api.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Qualifier;
import practice.spring_gym_api.dto.CoachDTO;
import practice.spring_gym_api.dto.CoachMapper;
import practice.spring_gym_api.dto.MemberDTO;
import practice.spring_gym_api.dto.MemberMapper;
import practice.spring_gym_api.dto.request.CoachRequestDTO;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.spring_gym_api.service.CoachService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * REST controller for managing coaches.
 * Provides endpoints for CRUD operations, pagination, and analytical queries
 * such as retrieving the coach with the most or fewest clients.
 */
@OpenAPIDefinition(info =
@Info(title = "Gym API", version = "1.1", description = "API for managing gym operations"))
@RestController
@RequestMapping(path = "api/v1/gym-api")
public class CoachController {

    private final CoachService coachService;
    private final CoachMapper coachMapper;
    private final MemberMapper memberMapper;

    public CoachController(CoachService coachService, @Qualifier("coachMapperimpl") CoachMapper coachMapper, MemberMapper memberMapper) {
        this.coachService = coachService;
        this.coachMapper= coachMapper;
        this.memberMapper = memberMapper;
    }

    /**
     * Retrieves a coach by their ID.
     *
     * @param id the ID of the coach
     * @return the coach as a DTO
     */
    @Operation(summary = "Retrieves the coach associated with the specified id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Coach successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Bad request due to invalid input")
    })
    @GetMapping(path = "/coaches/{coach_id}")
    public ResponseEntity<CoachDTO> getCoachById(
            @Parameter(name = "coach_id", description = "ID of the coach", required = true)
            @PathVariable("coach_id") Long id) {
        CoachEntity coachEntity = coachService.getCoachById(id);
        CoachDTO coachDTO = coachMapper.convertToCoachDto(coachEntity);
        return ResponseEntity.ok(coachDTO);
    }

    /**
     * Retrieves a paginated list of all coaches.
     *
     * @param page the page number (default = 0)
     * @param size the page size (default = 5)
     * @return list of CoachDTOs
     */
    @Operation(summary = "Retrieves a paginated list of all coaches")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved paginated list of coaches"),
            @ApiResponse(responseCode = "400", description = "Bad request due to invalid pagination parameters")
    })
    @GetMapping(path = "/coaches")
    public List<CoachDTO> getAllCoaches(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CoachEntity> coachEntities = coachService.getAllCoachesPageable(pageable);

        return coachEntities.stream()
                .map(coachMapper::convertToCoachDto)
                .toList();
    }

    /**
     * Returns the coach who has the highest number of clients.
     *
     * @return the coach as a DTO
     */
    @Operation(summary = "Returns the coach who has the highest number of clients")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Coach with the highest number of clients retrieved"),
            @ApiResponse(responseCode = "400", description = "Coach data not found or invalid")
    })
    @GetMapping (path = "/coaches/most-clients")
    public ResponseEntity<CoachDTO> getCoachWithTheHighestAmountOfClients(){
        CoachEntity coachEntity = coachService.getCoachWithHighestClients();
        CoachDTO coachDTO = coachMapper.convertToCoachDto(coachEntity);
        return ResponseEntity.ok(coachDTO);
    }

    /**
     * Returns the coach who has the lowest number of clients.
     *
     * @return the coach as a DTO
     */
    @Operation(summary = "Returns the coach who has the lowest number of clients")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Coach with the lowest number of clients retrieved"),
            @ApiResponse(responseCode = "400", description = "Coach data not found or invalid")
    })
    @GetMapping (path = "/coaches/least-clients")
    public ResponseEntity<CoachDTO> getCoachWithTheLowestAmountOfClients(){
        CoachEntity coachEntity = coachService.getCoachWithLowestClients();
        CoachDTO coachDTO = coachMapper.convertToCoachDto(coachEntity);
        return ResponseEntity.ok(coachDTO);
    }

    /**
     * Returns all the clients of a coach.
     *
     * @param id the id used to identify the coach
     * @return the set of the coaches clients
     */
    @Operation(summary = "Returns all clients assigned to a coach")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved clients of the coach"),
            @ApiResponse(responseCode = "400", description = "Invalid coach ID or no clients found")
    })
    @GetMapping (path = "/coaches/{coach_id}/clients")
    public Set<MemberDTO> getAllClientsOfACoach(@PathVariable("coach_id") Long id) {
        Set<MemberEntity> memberEntities = coachService.getAllClientsByCoachId(id);

        return memberEntities.stream()
                .map(memberMapper::convertToMemberDTO)
                .collect(Collectors.toSet());
    }

    /**
     * Retrieves the workout plans associated with a coach, using their name.
     *
     * @param name the coach's name
     * @return list of workout plans
     */
    @Operation(summary = "Retrieves the workout plans associated with a coach by name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Workout plans successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid coach name or plans not found")
    })
    @GetMapping (path = "/coaches/{coach_name}/plans")
    public ResponseEntity<List<String>> getWorkoutPlansByCoachName(@PathVariable(name = "coach_name") String name){
        List<String> workoutPlans = coachService.getWorkoutPlansByCoachName(name);
        return ResponseEntity.ok(workoutPlans);
    }

    /**
     * Retrieves all available coaches (coaches with no clients).
     *
     * @return List of available CoachDTOs
     */
    @Operation(summary = "Retrieves all available coaches with no clients")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved available coaches")
    })
    @GetMapping (path = "/coaches/no-clients")
    public List<CoachDTO> getAvaliableCoaches(){
        List<CoachEntity> coachEntities = coachService.getAllCoachesThatAreAvaliable();

        return coachEntities.stream()
                .map(coachMapper::convertToCoachDto)
                .toList();
    }

    /**
     * Retrieves a coach by their ID and worker code.
     *
     * @param id    ID of the coach
     * @param code  Coach code to verify
     * @return      Matching CoachDTO
     */
    @Operation(summary = "Retrieves a coach using their ID and verification code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully verified and retrieved coach"),
            @ApiResponse(responseCode = "400", description = "Invalid ID or coach code")
    })
    @GetMapping (path = "/coaches/{coach_id}/verify")
    public ResponseEntity<CoachDTO> getCoachByIdAndCode(
            @PathVariable("coach_id") Long id,
            @RequestParam String code
    ) {
        CoachDTO coachDTO =  coachMapper.convertToCoachDto(coachService.getCoachByCoachCode(id, code));
        return ResponseEntity.ok(coachDTO);
    }

    /**
     * Registers a new coach in the system.
     *
     * @param coachRequestDTO the coach entity to register
     */
    @Operation(summary = "Registers a new coach")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Coach successfully registered"),
            @ApiResponse(responseCode = "400", description = "Bad request due to invalid coach data")
    })
    @PostMapping (path = "/coaches")
    public void registerNewCoach(@Valid @RequestBody CoachRequestDTO coachRequestDTO){
        coachService.registerNewCoach(coachRequestDTO);
    }

    /**
     * Registers multiple coaches in bulk.
     *
     * @param coachRequestDTOS object containing a list of coaches
     */
    @Operation(summary = "Registers multiple coaches in a batch")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Batch registration successful"),
            @ApiResponse(responseCode = "400", description = "Bad request due to invalid batch input")
    })
    @PostMapping(path = "/coaches/batch")
    public void registerNewCoaches(@Valid @RequestBody List<CoachRequestDTO> coachRequestDTOS) {
        coachService.registerNewCoaches(coachRequestDTOS);
    }

    /**
     * Updates the name of a coach by their ID.
     *
     * @param id the coach ID
     * @param name the new name
     */
    @Operation(summary = "Updates the coach's name by ID and email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Coach name updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request due to invalid ID or email")
    })
    @PatchMapping(path = "/coaches/{coach_id}/name")
    public void updateNameByIdAndEmail(
            @PathVariable("coach_id") Long id,
            @RequestParam String name,
            @RequestParam String email
    ) {
        coachService.updateNameByIdAndEmail(id, name, email);
    }

    /**
     * Updates the list of clients (members) assigned to a coach.
     *
     * @param id the coach ID
     * @param memberEntitySet the new set of clients
     */
    @Operation(summary = "Adds new clients to the coach by ID and email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clients successfully added"),
            @ApiResponse(responseCode = "400", description = "Bad request due to invalid coach or client data")
    })
    @PatchMapping(path = "/coaches/{coach_id}/clients/add")
    public void addClientsByIdAndEmail(
            @PathVariable("coach_id") Long id,
            @RequestParam String email,
            @Valid @RequestBody Set<MemberEntity> memberEntitySet
    ) {
        coachService.addClientsByIdAndEmail(id, email, memberEntitySet);
    }

    /**
     * Replaces a coach's entire client list by ID and email.
     *
     * @param id             Coach ID
     * @param email          Coach email
     * @param ids Id's of new clients
     */
    @Operation(summary = "Replaces a coach's entire client list")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client list successfully replaced"),
            @ApiResponse(responseCode = "400", description = "Bad request due to invalid data")
    })
    @PatchMapping(path = "/coaches/{coach_id}/clients/replace")
    public void replaceCLientListByIdAndEmail(
            @PathVariable("coach_id") Long id,
            @RequestParam String email,
            @RequestBody List<Long> ids
    )  {
        coachService.replaceClientListByIdAndEmail(id, email, ids);
    }

    /**
     * Updates a coach's role to MEMBER or WORKER.
     * Deletes the original coach and saves a new entity of the target role.
     *
     * @param id    Coach ID
     * @param email Coach email
     * @param role  New role (ROLE_MEMBER or ROLE_WORKER)
     */
    @Operation(summary = "Changes the role of a coach to MEMBER or WORKER")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Coach role successfully updated"),
            @ApiResponse(responseCode = "400", description = "Bad request due to invalid role or email")
    })
    @PatchMapping(path = "/coaches/{coach_id}/role")
    public void updateRoleOfACoach(
            @PathVariable("coach_id") Long id,
            @RequestParam String email,
            @RequestParam String role
    ) {
        coachService.updateRoleOfACoach(id, email, role);
    }

    /**
     * Performs a full update on a coach using both ID and email for verification.
     *
     * @param id the coach ID
     * @param email the email of the coach
     * @param coachRequestDTO the updated coach object
     */
    @Operation(summary = "Fully updates a coach's details by ID and email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Coach updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request due to invalid coach data")
    })
    @PutMapping(path = "/coaches/{coach_id}")
    public void updateCoachByIdAndEmail(
            @PathVariable("coach_id") Long id,
            @RequestParam String email,
            @Valid @RequestBody CoachRequestDTO coachRequestDTO
    ) {
        coachService.updateCoachByIdAndEmail(id, email, coachRequestDTO);
    }

    /**
     * Updates the workout plans of a coach by ID and email.
     *
     * @param id           Coach ID
     * @param email        Coach email for verification
     * @param workoutPlans List of new workout plans to assign
     */
    @Operation(summary = "Updates a coach's workout plans by ID and email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Workout plans updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request due to invalid input")
    })
    @PatchMapping(path = "/coaches/{coach_id}/plans")
    public void updateWorkoutPlans(
            @PathVariable("coach_id") Long id,
            @RequestParam String email,
            @NotNull(message = "Workout plans list must not be null")
            @Size(min = 1, message = "At least one workout plan must be provided")
            @Valid @RequestBody List<String> workoutPlans
    ) {
        coachService.updateWorkoutPlans(id, email, workoutPlans);
    }

    /**
     * Updates the coach code of a specific coach by ID.
     *
     * @param id    ID of the coach
     * @param email Email of the coach for verification
     * @param coachCode  New coach code to assign
     */
    @Operation(summary = "Updates a coach's unique code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Coach code updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request due to invalid data")
    })
    @PatchMapping (path = "/coaches/{coach_id}/code")
    public void updateCodeOfACoach(
            @PathVariable("coach_id") Long id,
            @RequestParam String email,
            @RequestParam String coachCode
    ) {
        coachService.updateCodeOfACoach(id, email, coachCode);
    }

    /**
     * Deletes a coach by their ID.
     *
     * @param id the coach ID
     */
    @Operation(summary = "Deletes a coach by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Coach deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request due to invalid ID")
    })
    @DeleteMapping(path = "/coaches/{coach_id}")
    public void deleteCoach(@PathVariable("coach_id") Long id){
        coachService.deleteCoachById(id);
    }

    /**
     * Deletes all coaches in the system.
     */
    @Operation(summary = "Deletes all coaches")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All coaches deleted successfully")
    })
    @DeleteMapping(path = "/coaches")
    public void deleteAllCoaches(){
        coachService.deleteAllCoaches();
    }
}
