package practice.spring_gym_api.testdata.dto;

import practice.spring_gym_api.dto.CoachDTO;

import java.time.LocalDate;

public class CoachDTOTestData {

    public static CoachDTO createSeedDTOCoach1(){
        CoachDTO coachDTO1 = new CoachDTO(
                "Alex Smith",
                45,
                LocalDate.of(1980, 4, 12)
        );
        return coachDTO1;
    }
    public static CoachDTO createSeedDTOCoach2(){
        CoachDTO coachDTO2 = new CoachDTO(
                "Maria Gonzalez",
                39,
                LocalDate.of(1985, 11, 3)
        );
        return coachDTO2;
    }
}
