package practice.spring_gym_api.testdata.entity;

import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.entity.enums.Roles;

import java.time.LocalDate;
import java.util.List;

public class MemberTestData {

        CoachEntity coach1 = new CoachEntity(
                "Alex Smith",
                LocalDate.of(1980, 4, 12),
                Roles.ROLE_COACH,
                "alexSmith@gmail.com",
                List.of("FBEOD" , "Upper/Lower"),
                "EMP-990X-YTR8"
        );
        CoachEntity coach2 = new CoachEntity(
                "Maria Gonzalez",
                LocalDate.of(1985, 11, 3),
                Roles.ROLE_COACH,
                "mariaGonzalez@gmail.com",
                List.of("PPL/Upper Lower", "PPL/Arnold"),
                "WKRCODE-4583"
        );

        public MemberEntity createSeedMember1(){
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
            member1.setCoachedBy(coach1);
            return member1;
        }

        public MemberEntity createSeedMember2() {
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
            member2.setCoachedBy(coach2);
            return member2;
        }

        public MemberEntity createSeedMember3 () {
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
            member3.setCoachedBy(coach1);
            return member3;
        }

        public MemberEntity createSeedMember4 () {
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
            member4.setCoachedBy(coach2);
            return member4;
        }

        public MemberEntity createSeedMember5 () {
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
            member5.setCoachedBy(coach1);
            return member5;
        }
}
