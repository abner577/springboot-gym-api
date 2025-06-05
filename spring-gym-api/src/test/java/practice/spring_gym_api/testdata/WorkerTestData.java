package practice.spring_gym_api.testdata;

import entity.WorkerEntity;
import entity.enums.Roles;

import java.time.LocalDate;

public class WorkerTestData {

    public WorkerEntity createSeedWorker1(){
        WorkerEntity worker1 = new WorkerEntity(
                "Rachel Thomas",
                LocalDate.of(1985, 2, 14),
                Roles.ROLE_WORKER
        );
        return worker1;
    }

    public WorkerEntity createSeedWorker2(){
       WorkerEntity worker2 = new WorkerEntity(
               "James Wu",
               LocalDate.of(1990, 10, 30),
               Roles.ROLE_WORKER
       );
       return worker2;
    }
}
