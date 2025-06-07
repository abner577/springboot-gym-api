package practice.spring_gym_api.entity.wrapper;

import practice.spring_gym_api.entity.MemberEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import java.util.List;

public class MemberListWrapper {
    @Size(min = 2, message = "Must register at least 2 members.")
    private List<@Valid  MemberEntity> memberList;

    public List<MemberEntity> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<MemberEntity> memberList) {
        this.memberList = memberList;
    }
}
