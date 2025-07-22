package practice.spring_gym_api.testdata.dto;

import practice.spring_gym_api.dto.MemberDTO;

import java.time.LocalDate;

public class MemberDTOTestData {

    public static MemberDTO createdSeedMemberDTO1(){
        MemberDTO member1 = new MemberDTO(
                "John Doe",
                30,
                LocalDate.of(1995, 3, 15)
        );
        return member1;
    }
    public static MemberDTO createdSeedMemberDTO2(){
        MemberDTO member2 = new MemberDTO(
                "Jane Smith",
                35,
                LocalDate.of(1990, 7, 20)
        );
        return member2;
    }
    public static MemberDTO createdSeedMemberDTO3(){
        MemberDTO member3 = new MemberDTO(
                "David Lee",
                36,
                LocalDate.of(1988, 6, 10)
        );
        return member3;
    }
    public static MemberDTO createdSeedMemberDTO4(){
        MemberDTO member4 = new MemberDTO(
                "Emily Chen",
                27,
                LocalDate.of(1997, 9, 1)
        );
        return member4;
    }
    public static MemberDTO createdSeedMemberDTO5(){
        MemberDTO member5 = new MemberDTO(
                "Carlos Rivera",
                32,
                LocalDate.of(1992, 12, 5)
        );
        return member5;
    }
}
