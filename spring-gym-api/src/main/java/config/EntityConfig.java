package config;

import entity.CoachEntity;
import entity.MemberEntity;
import entity.WorkerEntity;
import entity.enums.Roles;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import repository.CoachRepository;
import repository.MemberRepository;
import repository.WorkerRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class EntityConfig {

    // ───── Seed Members ─────
    @Bean
    CommandLineRunner seedMembers(MemberRepository memberRepository){
        return args -> {
            List<MemberEntity> listOfMembers = new ArrayList<>();

            MemberEntity member1 = new MemberEntity();
            member1.setName("John Doe");
            member1.setDateOfBirth(LocalDate.of(1995, 3, 15));
            member1.setCoachedBy("Coach Alex");
            member1.setMembershipDate("2024-01-10");
            member1.setRole(Roles.ROLE_MEMBER);
            member1.setBench(225);
            member1.setSquat(275);
            member1.setDeadlift(315);
            member1.setTotal(225 + 275 + 315);

            MemberEntity member2 = new MemberEntity();
            member2.setName("Jane Smith");
            member2.setDateOfBirth(LocalDate.of(1990, 7, 22));
            member2.setCoachedBy("Coach Maria");
            member2.setMembershipDate("2023-11-02");
            member2.setRole(Roles.ROLE_MEMBER);
            member2.setBench(135);
            member2.setSquat(185);
            member2.setDeadlift(200);
            member2.setTotal(135 + 185 + 200);

            MemberEntity member3 = new MemberEntity();
            member3.setName("David Lee");
            member3.setDateOfBirth(LocalDate.of(1988, 6, 10));
            member3.setCoachedBy("Alex Smith");
            member3.setMembershipDate("2023-05-20");
            member3.setRole(Roles.ROLE_MEMBER);
            member3.setBench(225);
            member3.setSquat(315);
            member3.setDeadlift(405);
            member3.setTotal(225 + 315 + 405);

            MemberEntity member4 = new MemberEntity();
            member4.setName("Emily Chen");
            member4.setDateOfBirth(LocalDate.of(1997, 9, 1));
            member4.setCoachedBy("Maria Gonzalez");
            member4.setMembershipDate("2024-03-01");
            member4.setRole(Roles.ROLE_MEMBER);
            member4.setBench(135);
            member4.setSquat(205);
            member4.setDeadlift(295);
            member4.setTotal(135 + 205 + 295);

            MemberEntity member5 = new MemberEntity();
            member5.setName("Carlos Rivera");
            member5.setDateOfBirth(LocalDate.of(1992, 12, 5));
            member5.setCoachedBy("Alex Smith");
            member5.setMembershipDate("2023-08-15");
            member5.setRole(Roles.ROLE_MEMBER);
            member5.setBench(200);
            member5.setSquat(250);
            member5.setDeadlift(300);
            member5.setTotal(200 + 250 + 300);

            listOfMembers.add(member1);
            listOfMembers.add(member2);
            listOfMembers.add(member3);
            listOfMembers.add(member4);
            listOfMembers.add(member5);
            memberRepository.saveAll(listOfMembers);
        };
    }
    // ───── Seed Coaches ─────
    @Bean
    CommandLineRunner seedCoaches(CoachRepository coachRepository) {
        return args -> {
            List<CoachEntity> listOfCoaches = new ArrayList<>();
            CoachEntity coach1 = new CoachEntity();
            coach1.setName("Alex Smith");
            coach1.setDateOfBirth(LocalDate.of(1980, 4, 12));
            coach1.setRole(Roles.ROLE_COACH);
            coach1.setClients(new ArrayList<>());
            coach1.setWorkoutPlans(List.of("Full Body Split", "Hypertrophy Phase 1"));
            coach1.setAge(0); // Will be dynamically calculated

            CoachEntity coach2 = new CoachEntity();
            coach2.setName("Maria Gonzalez");
            coach2.setDateOfBirth(LocalDate.of(1985, 11, 3));
            coach2.setRole(Roles.ROLE_COACH);
            coach2.setClients(new ArrayList<>());
            coach2.setWorkoutPlans(List.of("Rehab Cycle", "Powerlifting Peak Program"));
            coach2.setAge(0);

            listOfCoaches.add(coach1);
            listOfCoaches.add(coach2);
            coachRepository.save(coach1);
        };
    }
    // ───── Seed Workers ─────
    @Bean
    CommandLineRunner seedWorkers(WorkerRepository workerRepository) {
        return args -> {
            List<WorkerEntity> listOfWorkers = new ArrayList<>();
            WorkerEntity worker1 = new WorkerEntity();
            worker1.setName("Rachel Thomas");
            worker1.setDateOfBirth(LocalDate.of(1985, 2, 14));
            worker1.setRole(Roles.ROLE_WORKER);
            worker1.setAge(0); // Calculated dynamically

            WorkerEntity worker2 = new WorkerEntity();
            worker2.setName("James Wu");
            worker2.setDateOfBirth(LocalDate.of(1990, 10, 30));
            worker2.setRole(Roles.ROLE_WORKER);
            worker2.setAge(0);

            listOfWorkers.add(worker1);
            listOfWorkers.add(worker2);
            workerRepository.saveAll(listOfWorkers);

        };
    }

}
