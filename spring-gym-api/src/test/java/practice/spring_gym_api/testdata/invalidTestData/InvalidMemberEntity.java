package practice.spring_gym_api.testdata.invalidTestData;

import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.entity.enums.Roles;
import practice.spring_gym_api.testdata.entity.CoachTestData;

import java.time.LocalDate;

public class InvalidMemberEntity {
    public static MemberEntity createInvalidSeedMember1(){
        MemberEntity member1 = new MemberEntity(
                "John Doe",
                LocalDate.of(1995, 3, 15),
                "2024-01-10",
                "johnDoe@gmail.com",
                Roles.ROLE_MEMBER,
                225,
                275,
                315,
                225 + 275 +315
        );
        return member1;
    }

    public static MemberEntity createInvalidSeedMember2() {
        MemberEntity member2 = new MemberEntity(
                "Jane Smith",
                LocalDate.of(1990, 7, 22),
                "2023-11-02",
                "janeSmith@gmail.com",
                Roles.ROLE_MEMBER,
                135,
                185,
                200,
                135 + 185 + 200
        );
        return member2;
    }

    public static MemberEntity createInvalidSeedMember3 () {
        MemberEntity member3 = new MemberEntity(
                "David Lee",
                LocalDate.of(1988, 6, 10),
                "2023-05-20",
                "davidLee@gmail.com",
                Roles.ROLE_MEMBER,
                225,
                315,
                405,
                225 + 315 + 405
        );
        return member3;
    }

    public static MemberEntity createInvalidSeedMember4 () {
        MemberEntity member4 = new MemberEntity(
                "Emily Chen",
                LocalDate.of(1997, 9, 1),
                "2024-03-01",
                "emelyChen@gmail.com",
                Roles.ROLE_MEMBER,
                115,
                145,
                160,
                115 + 145 + 160
        );
        return member4;
    }

    public static MemberEntity createInvalidSeedMember5 () {
        MemberEntity member5 = new MemberEntity(
                "Carlos Rivera",
                LocalDate.of(1992, 12, 5),
                "2023-08-15",
                "carlosRivera@gmail.com",
                Roles.ROLE_MEMBER,
                200,
                250,
                275,
                200 + 250 + 275
        );
        return member5;
    }
}
