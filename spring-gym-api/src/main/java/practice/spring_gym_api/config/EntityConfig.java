package practice.spring_gym_api.config;

import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.entity.MemberEntity;
import practice.spring_gym_api.entity.WorkerEntity;
import practice.spring_gym_api.entity.enums.Roles;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import practice.spring_gym_api.repository.CoachRepository;
import practice.spring_gym_api.repository.MemberRepository;
import practice.spring_gym_api.repository.WorkerRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Configuration
public class EntityConfig {

    // ───── Seed Members & Coaches ─────
    @Bean
    CommandLineRunner seedMembersAndCoaches(MemberRepository memberRepository, CoachRepository coachRepository){
        return args -> {
            List<MemberEntity> listOfMembers = new ArrayList<>();
            List<CoachEntity> listOfCoaches = new ArrayList<>();

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

            // set members coaches
            member1.setCoachedBy(coach1);
            member2.setCoachedBy(coach2);
            member3.setCoachedBy(coach1);
            member4.setCoachedBy(coach2);
            member5.setCoachedBy(coach1);

            // set coaches client list
            coach1.getClients().addAll(List.of(member1, member3, member5));
            coach2.getClients().addAll(List.of(member2, member4));

            // add coaches to coachesList
            listOfCoaches.add(coach1);
            listOfCoaches.add(coach2);

            // add members to membersList
            listOfMembers.add(member1);
            listOfMembers.add(member2);
            listOfMembers.add(member3);
            listOfMembers.add(member4);
            listOfMembers.add(member5);

            coachRepository.saveAll(listOfCoaches);
            memberRepository.saveAll(listOfMembers);
        };
    }
    // ───── Seed Workers ─────
    @Bean
    CommandLineRunner seedWorkers(WorkerRepository workerRepository) {
        return args -> {
            List<WorkerEntity> listOfWorkers = new ArrayList<>();

            WorkerEntity worker1 = new WorkerEntity(
                    "Rachel Thomas",
                    LocalDate.of(1985, 2, 14),
                    Roles.ROLE_WORKER,
                    "rachelThomas@gmail.com",
                    "WKR-8372-LKJD"
            );

            WorkerEntity worker2 = new WorkerEntity(
                    "James Wu",
                    LocalDate.of(1990, 10, 30),
                    Roles.ROLE_WORKER,
                    "jamesWu@gmail.com",
                    "WRK2024-AZ19"
            );

                    listOfWorkers.add(worker1);
                    listOfWorkers.add(worker2);
                    workerRepository.saveAll(listOfWorkers);
        };
    }
}
