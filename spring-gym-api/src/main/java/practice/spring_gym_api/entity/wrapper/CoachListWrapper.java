package practice.spring_gym_api.entity.wrapper;

import practice.spring_gym_api.entity.CoachEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import java.util.List;

public class CoachListWrapper {
    @Size(min = 2, message = "Must register at least 2 coaches.")
    private List<@Valid CoachEntity> coachList;

    public List<CoachEntity> getCoachList() {
        return coachList;
    }

    public void setCoachList(List<CoachEntity> coachList) {
        this.coachList = coachList;
    }
}
