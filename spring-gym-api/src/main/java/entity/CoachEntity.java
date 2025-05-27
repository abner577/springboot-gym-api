package entity;

import entity.enums.Roles;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Entity
@Table(name = "coaches")
public class CoachEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private Roles role;

    @OneToMany(mappedBy = "coaches", cascade = {
            CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH
    })
    private List<MemberEntity> clients;
    private List<String> workoutPlans;
    @Transient
    private int age;

    public CoachEntity(Long id, String name, LocalDate dateOfBirth, Roles role, List<MemberEntity> clients, List<String> workoutPlans) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.clients = clients;
        this.workoutPlans = workoutPlans;
    }
    public CoachEntity(String name, LocalDate dateOfBirth, Roles role, List<MemberEntity> clients, List<String> workoutPlans) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.clients = clients;
        this.workoutPlans = workoutPlans;
    }

    public CoachEntity() {}

    public CoachEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return Period.between(this.dateOfBirth, LocalDate.now()).getYears();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {this.role = role;}

    public List<MemberEntity> getClients() {
        return clients;
    }

    public void setClients(List<MemberEntity> clients) {
        this.clients = clients;
    }

    public List<String> getWorkoutPlans() {
        return workoutPlans;
    }

    public void setWorkoutPlans(List<String> workoutPlans) {
        this.workoutPlans = workoutPlans;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
