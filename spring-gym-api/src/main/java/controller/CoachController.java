package controller;

import dto.CoachDTO;
import dto.CoachMapper;
import entity.CoachEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import service.CoachService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/gym-api")
public class CoachController {

    private final CoachService coachService;
    private final CoachMapper coachMapper;

    public CoachController(CoachService coachService, CoachMapper coachMapper) {
        this.coachService = coachService;
        this.coachMapper= coachMapper;
    }
    @GetMapping(path = "/coach/{coach_id}")
    public CoachDTO getCoachById(@PathVariable("coach_id") Long id) {
        CoachEntity coachEntity = coachService.getCoachById(id);
        CoachDTO coachDTO = coachMapper.convertToCoachDto(coachEntity);
        return coachDTO;
    }

    @GetMapping(path = "/coaches")
    public Page<CoachDTO> getAllCoaches(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        List<CoachDTO> coachDTOSToReturn = new ArrayList<>();
        Page<CoachEntity> coachEntities = coachService.getAllCoachesPageable(pageable);
        if(coachEntities.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No coaches avaliable");

        for(CoachEntity coachEntity : coachEntities) {
            CoachDTO coachDTO =  coachMapper.convertToCoachDto(coachEntity);
            coachDTOSToReturn.add(coachDTO);
        }
        return (Page<CoachDTO>) coachDTOSToReturn;
    }

    @GetMapping (path = "/best/coach")
    public CoachDTO getCoachWithTheHighestAmountOfClients1(){
        List<CoachEntity> entityList = coachService.getAllCoaches();
        if(entityList.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No coaches avaliable");
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
}
