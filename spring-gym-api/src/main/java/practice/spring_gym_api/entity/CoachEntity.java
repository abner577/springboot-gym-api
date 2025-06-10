package practice.spring_gym_api.entity;

import practice.spring_gym_api.entity.enums.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "coaches")
public class CoachEntity {
    @Id
    @SequenceGenerator(
            name = "coach_sequence",
            sequenceName = "coach_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "coach_sequence"
            )
    private Long id;

    @NotBlank(message = "Name is Required")
    private String name;

    @NotBlank(message = "Email is Required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull
    @Past
    private LocalDate dateOfBirth;

    @NotNull(message = "Role is Required")
    @Enumerated(EnumType.STRING)
    private Roles role;

    @OneToMany(mappedBy = "coachedBy", cascade = {
            CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH
    })
    @NotEmpty
    private Set<MemberEntity> clients;

    @ElementCollection
    @NotNull(message = "Workout plans list must not be null")
    @Size(min = 1, message = "At least one workout plan must be provided")
    private List<@NotBlank(message = "Workout plans are required") String> workoutPlans;
    @Transient
    private int age;

    public CoachEntity(String name, LocalDate dateOfBirth, Roles role, Set<MemberEntity> clients, String email, List<String> workoutPlans) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.clients = clients;
        this.email = email;
        this.workoutPlans = workoutPlans;
    }

    public CoachEntity() {}

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

    public LocalDate getDateOfBirth() {return this.dateOfBirth;}

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {this.role = role;}

    public Set<MemberEntity> getClients() {
        return clients;
    }

    public void setClients(Set<MemberEntity> clients) {
        this.clients = clients;
    }

    public List<String> getWorkoutPlans() {
        return workoutPlans;
    }

    public void setWorkoutPlans(List<String> workoutPlans) {
        this.workoutPlans = workoutPlans;
    }

    public int getAge() {
        return Period.between(this.dateOfBirth, LocalDate.now()).getYears();
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}
}
