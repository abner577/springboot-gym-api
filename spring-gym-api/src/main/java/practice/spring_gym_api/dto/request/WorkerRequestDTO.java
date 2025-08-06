package practice.spring_gym_api.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import practice.spring_gym_api.entity.enums.Roles;

import java.time.LocalDate;

public class WorkerRequestDTO {
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

    public void setRole(Roles role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWorkerCode() {
        return workerCode;
    }

    public void setWorkerCode(String workerCode) {
        this.workerCode = workerCode;
    }

    public WorkerRequestDTO(String name, LocalDate dateOfBirth, Roles role, String email, String workerCode) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.email = email;
        this.workerCode = workerCode;
    }
}
