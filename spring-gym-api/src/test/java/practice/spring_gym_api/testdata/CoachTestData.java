package practice.spring_gym_api.testdata;

import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.entity.enums.Roles;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CoachTestData {

    public static CoachEntity createSeedCoach1() {
        CoachEntity coach1 = new CoachEntity(
                1L,
                "Alex Smith",
                LocalDate.of(1980, 4, 12),
                Roles.ROLE_COACH,
                "alexSmith@gmail.com",
                List.of("FBEOD" , "Upper/Lower"),
                "EMP-990X-YTR8"
        );
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
        Set<MemberEntity> clientsForCoach1 = Set.of(member1, member3, member5);
        coach1.setClients(new HashSet<>(clientsForCoach1));
        return coach1;
    }

    public static CoachEntity createSeedCoach2() {
        CoachEntity coach2 = new CoachEntity(
                2L,
                "Maria Gonzalez",
                LocalDate.of(1985, 11, 3),
                Roles.ROLE_COACH,
                "mariaGonzalez@gmail.com",
                List.of("PPL/Upper Lower", "PPL/Arnold"),
                "WKRCODE-4583"
        );
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
        Set<MemberEntity> clientsForCoach2 = Set.of(member2, member4);
        coach2.setClients(new HashSet<>(clientsForCoach2));
        return coach2;
    }
}
