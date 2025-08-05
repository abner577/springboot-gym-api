package practice.spring_gym_api.dto.request;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import practice.spring_gym_api.entity.enums.Roles;

import java.time.LocalDate;
import java.util.List;

public class CoachRequestDTO {

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

    @ElementCollection
    @NotNull(message = "Workout plans list must not be null")
    @Size(min = 1, message = "At least one workout plan must be provided")
    private List<@NotBlank(message = "Workout plans are required") String> workoutPlans;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void setRole(Roles role) {
        this.role = role;
    }

    public List<String> getWorkoutPlans() {
        return workoutPlans;
    }

    public void setWorkoutPlans(List<String> workoutPlans) {
        this.workoutPlans = workoutPlans;
    }

    public CoachRequestDTO(String name, String email, LocalDate dateOfBirth, Roles role, List<String> workoutPlans) {
        this.name = name;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.workoutPlans = workoutPlans;
    }
}
