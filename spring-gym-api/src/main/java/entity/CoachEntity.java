package entity;

import entity.enums.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.List;

@Entity
@Table(name = "coaches")
public class CoachEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "Name is Required")
    private String name;

    @NotBlank(message = "Date of Birth is Required")
    @Past
    private LocalDate dateOfBirth;

    @NotBlank(message = "Role is Required")
    @Enumerated(EnumType.STRING)
    private Roles role;

    @OneToMany(mappedBy = "coachedBy", cascade = {
            CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH
    })
    @NotEmpty
    private HashSet<MemberEntity> clients;

    @ElementCollection
    private List<@NotBlank(message = "Workout plans are required") String> workoutPlans;
    @Transient
    private int age;

    public CoachEntity(String name, LocalDate dateOfBirth, Roles role, HashSet<MemberEntity> clients, List<String> workoutPlans) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.clients = clients;
        this.workoutPlans = workoutPlans;
    }

    public Long getId() {return id;}

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDateOfBirth() {
        return Period.between(this.dateOfBirth, LocalDate.now()).getYears();
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {this.role = role;}

    public HashSet<MemberEntity> getClients() {
        return clients;
    }

    public void setClients(HashSet<MemberEntity> clients) {
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
