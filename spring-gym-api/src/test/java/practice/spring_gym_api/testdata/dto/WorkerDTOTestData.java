package practice.spring_gym_api.testdata.dto;

import practice.spring_gym_api.dto.WorkerDTO;

import java.time.LocalDate;

public class WorkerDTOTestData {

    public static WorkerDTO createdSeedWorkerDTO1(){
        WorkerDTO workerDTO1 = new WorkerDTO(
                "Rachel Thomas",
                40,
                LocalDate.of(1985, 2, 14)
        );
        return workerDTO1;
    }
    public static WorkerDTO createdSeedWorkerDTO2(){
        WorkerDTO workerDTO2 = new WorkerDTO(
                "James Wu",
                34,
                LocalDate.of(1990, 10, 30)
        );
        return workerDTO2;
    }
}
