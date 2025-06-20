package practice.spring_gym_api.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Qualifier;
import practice.spring_gym_api.dto.CoachDTO;
import practice.spring_gym_api.dto.CoachMapper;
import practice.spring_gym_api.dto.MemberDTO;
import practice.spring_gym_api.dto.MemberMapper;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.entity.wrapper.CoachListWrapper;
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
    @GetMapping(path = "/coach/id/{coach_id}")
    public ResponseEntity<CoachDTO> getCoachById(@PathVariable("coach_id") Long id) {
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
    @GetMapping(path = "/coaches")
    public List<CoachDTO> getAllCoaches(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
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
    @GetMapping (path = "/best/coach")
    public CoachDTO getCoachWithTheHighestAmountOfClients(){
        List<CoachEntity> entityList = coachService.getAllCoaches();
        int max = entityList.get(0).getClients().size();
        CoachEntity entityToReturn = entityList.get(0);

        for(CoachEntity coachEntity : entityList){
            if(coachEntity.getClients().size() > max) {
                entityToReturn = coachEntity;
                max = coachEntity.getClients().size();
            }
        }
        CoachDTO coachDTO = coachMapper.convertToCoachDto(entityToReturn);
        return coachDTO;
    }

    /**
     * Returns the coach who has the lowest number of clients.
     *
     * @return the coach as a DTO
     */
    @GetMapping (path = "/worst/coach")
    public CoachDTO getCoachWithTheLowestAmountOfClients(){
        List<CoachEntity> entityList = coachService.getAllCoaches();
        int min = entityList.get(0).getClients().size();
        CoachEntity entityToReturn = entityList.get(0);

        for(CoachEntity coachEntity : entityList){
            if(coachEntity.getClients().size() < min) {
                entityToReturn = coachEntity;
                min = coachEntity.getClients().size();
            }
        }
        CoachDTO coachDTO = coachMapper.convertToCoachDto(entityToReturn);
        return coachDTO;
    }

    /**
     * Returns all the clients of a coach.
     *
     * @param id the id used to identify the coach
     * @return the set of the coaches clients
     */
    @GetMapping (path = "/clients/of/{coach_id}")
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
    @GetMapping (path = "/coach/name/{coach_name}")
    public List<String> getWorkoutPlansByCoachName(@PathVariable(name = "coach_name") String name){
        List<String> workoutPlans = coachService.getWorkoutPlansByCoachName(name);
        return workoutPlans;
    }

    @GetMapping (path = "/avaliable/coaches")
    public List<CoachDTO> getAvaliableCoaches(){
        List<CoachEntity> coachEntities = coachService.getAllCoachesThatAreAvaliable();

        return coachEntities.stream()
                .map(coachMapper::convertToCoachDto)
                .toList();
    }

    /**
     * Registers a new coach in the system.
     *
     * @param coachEntity the coach entity to register
     */
    @PostMapping (path = "/coach")
    public void registerNewCoach(@Valid @RequestBody CoachEntity coachEntity){
        coachService.registerNewCoach(coachEntity);
    }

    /**
     * Registers multiple coaches in bulk.
     *
     * @param coachListWrapper wrapper object containing a list of coaches
     */
    @PostMapping(path = "/coaches")
    public void registerNewCoaches(@Valid @RequestBody CoachListWrapper coachListWrapper) {
        coachService.registerNewCoaches(coachListWrapper.getCoachList());
    }

    /**
     * Updates the name of a coach by their ID.
     *
     * @param id the coach ID
     * @param name the new name
     */
    @PatchMapping(path = "/update/coach/name/{coach_id}")
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
    @PatchMapping(path = "/add/clients/{coach_id}")
    public void addClientsByIdAndEmail(
            @PathVariable("coach_id") Long id,
            @RequestParam String email,
            @Valid @RequestBody Set<MemberEntity> memberEntitySet
    ) {
        coachService.addClientsByIdAndEmail(id, email, memberEntitySet);
    }

    @PatchMapping(path = "/replace/coach/clients/{coach_id}")
    public void replaceCLientList(
            @PathVariable("coach_id") Long id,
            @RequestParam String email,
           @Valid @RequestBody Set<MemberEntity> memberEntities
    )  {
        coachService.replaceClientListByIdAndEmail(id, email, memberEntities);
    }

    @PatchMapping(path = "update/coach/role/{coach_id}")
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
     * @param coachEntity the updated coach object
     */
    @PutMapping(path = "/update/coach/{coach_id}")
    public void updateCoachByIdAndEmail(
            @PathVariable("coach_id") Long id,
            @RequestParam String email,
            @Valid @RequestBody CoachEntity coachEntity
    ) {
        coachService.updateCoachByIdAndEmail(id, email, coachEntity);
    }

    @PutMapping(path = "/update/workout/plans/{coach_id}")
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
     * Deletes a coach by their ID.
     *
     * @param id the coach ID
     */
    @DeleteMapping(path = "/coach/{coach_id}")
    public void deleteCoach(@PathVariable("coach_id") Long id){
        coachService.deleteCoachById(id);
    }

    /**
     * Deletes all coaches in the system.
     */
    @DeleteMapping(path = "/coach")
    public void deleteAllCoaches(){
        coachService.deleteAllCoaches();
    }
}
