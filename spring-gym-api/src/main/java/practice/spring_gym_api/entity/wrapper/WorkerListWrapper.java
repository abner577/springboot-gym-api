package practice.spring_gym_api.entity.wrapper;

import practice.spring_gym_api.entity.WorkerEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import java.util.List;

public class WorkerListWrapper {
    @Size(min = 2, message = "Must register at least 2 workers.")
    private List<@Valid WorkerEntity> workerList;

    public List<WorkerEntity> getWorkerList() {
        return workerList;
    }

    public void setWorkerList(List<WorkerEntity> workerList) {
        this.workerList = workerList;
    }
}
