package practice.spring_gym_api.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import practice.spring_gym_api.entity.enums.Roles;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

/**
 * Represents a non-coaching worker in the gym system, such as administrative or support staff.
 * This entity is mapped to the "workers" table in the database.
 */
@Entity
@Table(name = "workers")
public class WorkerEntity {

    /**
     * Primary key for the Worker table.
     * Uses a sequence generator to assign unique IDs.
     */
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

    @NotNull
    @Past
    private LocalDate dateOfBirth;

    @NotNull(message = "Role is Required")
    @Enumerated(EnumType.STRING)
    private Roles role;

    @NotBlank(message = "Email is Required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Worker Code is Required")
    private String workerCode;

    @Transient
    private int age;

    public WorkerEntity(String name, LocalDate dateOfBirth, Roles role, String email, String workerCode) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.email = email;
        this.workerCode = workerCode;
    }
    public WorkerEntity() {}

    public Long getId() {
        return id;
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
        return this.dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public int getAge() {
        return Period.between(this.dateOfBirth, LocalDate.now()).getYears();
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getWorkerCode() {
        return workerCode;
    }

    public void setWorkerCode(String workerCode) {
        this.workerCode = workerCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkerEntity that = (WorkerEntity) o;

        // If both have non-null IDs, compare IDs (standard persistence logic)
        if (id != null && that.id != null) {
            return Objects.equals(id, that.id);
        }

        // Otherwise, fallback to email
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
