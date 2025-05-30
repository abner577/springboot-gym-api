package controller;

import dto.CoachDTO;
import dto.CoachMapper;
import entity.CoachEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.CoachService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<CoachDTO> getAllCoaches(){
        List<CoachDTO> coachDTOSToReturn = new ArrayList<>();
        List<CoachEntity> coachEntities = coachService.getAllCoaches();
        for(CoachEntity coachEntity : coachEntities) {
            CoachDTO coachDTO =  coachMapper.convertToCoachDto(coachEntity);
            coachDTOSToReturn.add(coachDTO);
        }
        return coachDTOSToReturn;
    }
    /* Functional Programming approach to this method
        return coachService.getAllCoaches()
        .stream()
        .map(coachMapper::convertToCoachDto)
        .collect(Collectors.toList());
    */
}
