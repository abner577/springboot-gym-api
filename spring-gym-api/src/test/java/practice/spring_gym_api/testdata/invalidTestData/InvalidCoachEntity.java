package practice.spring_gym_api.testdata.invalidTestData;

import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.entity.enums.Roles;
import practice.spring_gym_api.testdata.entity.MemberTestData;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class InvalidCoachEntity {
    public static CoachEntity createInvalidSeedCoach1() {
        CoachEntity coach1 = new CoachEntity(
                1L,
                "Alex Smith",
                LocalDate.of(1980, 4, 12),
                Roles.ROLE_COACH,
                "alexSmith@gmail.com",
                List.of("FBEOD" , "Upper/Lower"),
                "EMP-990X-YTR8"
        );
        return coach1;
    }

    public static CoachEntity createInvalidSeedCoach2() {
        CoachEntity coach2 = new CoachEntity(
                2L,
                "Maria Gonzalez",
                LocalDate.of(1985, 11, 3),
                Roles.ROLE_COACH,
                "mariaGonzalez@gmail.com",
                List.of("Placeholder workouts "),
                "Placeholder coach code"
        );

        MemberEntity member2 = MemberTestData.createSeedMember2();
        MemberEntity member4 = MemberTestData.createSeedMember4();

        Set<MemberEntity> clientsForCoach2 = Set.of(member2, member4);
        coach2.setClients(clientsForCoach2);
        return coach2;
    }
}
