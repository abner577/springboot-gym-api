package practice.spring_gym_api.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import practice.spring_gym_api.dto.CoachDTO;
import practice.spring_gym_api.dto.CoachMapper;
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

    public CoachController(CoachService coachService, @Qualifier("coachMapperimpl") CoachMapper coachMapper) {
        this.coachService = coachService;
        this.coachMapper= coachMapper;
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
    public void updateNameById(
            @PathVariable("coach_id") Long id,
            @RequestParam(required = true) String name
    ) {
        coachService.updateNameById(id, name);
    }


    /**
     * Updates the list of clients (members) assigned to a coach.
     *
     * @param id the coach ID
     * @param memberEntitySet the new set of clients
     */
    @PatchMapping(path = "/update/coach/clients/{coach_id}")
    public void updateClientsById(
            @PathVariable("coach_id") Long id,
            @Valid @RequestBody Set<MemberEntity> memberEntitySet
    ) {
        coachService.updateClientsById(id, memberEntitySet);
    }

    /**
     * Performs a full update on a coach using both ID and email for verification.
     *
     * @param id the coach ID
     * @param name the new name
     * @param coachEntity the updated coach object
     */
    @PutMapping(path = "/update/coach/{coach_id}")
    public void updateCoachByIdAndEmail(
            @PathVariable("coach_id") Long id,
            @RequestParam(required = true) String name,
            @Valid @RequestBody CoachEntity coachEntity
    ) {
        coachService.updateCoachByIdAndEmail(id, name, coachEntity);
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
