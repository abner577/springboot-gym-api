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

@RestController
@RequestMapping(path = "api/v1/gym-api")
public class CoachController {

    private final CoachService coachService;
    private final CoachMapper coachMapper;

    public CoachController(CoachService coachService, @Qualifier("coachMapperimpl") CoachMapper coachMapper) {
        this.coachService = coachService;
        this.coachMapper= coachMapper;
    }
    @GetMapping(path = "/coach/id/{coach_id}")
    public ResponseEntity<CoachDTO> getCoachById(@PathVariable("coach_id") Long id) {
        CoachEntity coachEntity = coachService.getCoachById(id);
        CoachDTO coachDTO = coachMapper.convertToCoachDto(coachEntity);
        return ResponseEntity.ok(coachDTO);
    }

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

    @GetMapping (path = "/coach/name/{coach_name}")
    public List<String> getWorkoutPlansByCoachName(@PathVariable(name = "coach_name") String name){
        List<String> workoutPlans = coachService.getWorkoutPlansByCoachName(name);
        return workoutPlans;
    }

    @PostMapping (path = "/coach")
    public void registerNewCoach(@Valid @RequestBody CoachEntity coachEntity){
        coachService.registerNewCoach(coachEntity);
    }

    @PostMapping(path = "/coaches")
    public void registerNewCoaches(@Valid @RequestBody CoachListWrapper coachListWrapper) {
        coachService.registerNewCoaches(coachListWrapper.getCoachList());
    }

    @PatchMapping(path = "/update/coach/{coach_id}")
    public void updateNameOrClientsById(
            @PathVariable("coach_id") Long id,
            @RequestParam(required = false) String name,
            @RequestBody(required = false) List<MemberEntity> clients
    ) {
        coachService.updateNameOrClientsById(id, name, clients);
    }

    @PutMapping(path = "/update/coach/{coach_id}")
    public void updateCoach(
            @PathVariable("coach_id") Long id,
            @RequestBody CoachEntity coachEntity
    ) {
        coachService.updateCoachById(id, coachEntity);
    }

    @DeleteMapping(path = "/coach/{coach_id}")
    public void deleteCoach(@PathVariable("coach_id") Long id){
        coachService.deleteCoachById(id);
    }
    @DeleteMapping(path = "/coach")
    public void deleteAllCoaches(){
        coachService.deleteAllCoaches();
    }

}
