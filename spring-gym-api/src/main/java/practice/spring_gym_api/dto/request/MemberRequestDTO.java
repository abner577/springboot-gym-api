package practice.spring_gym_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import practice.spring_gym_api.entity.enums.Roles;

import java.time.LocalDate;

public class MemberRequestDTO {
    @NotBlank(message = "Name is Required")
    private String name;

    @NotNull
    @Past
    private LocalDate dateOfBirth;

    @NotBlank(message = "Membership date is Required")
    private String membershipDate;

    @NotBlank(message = "Email is Required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Role is Required")
    @Enumerated(EnumType.STRING)
    @Schema(example = "ROLE_MEMBER")
    private Roles role;

    @Min(value = 0, message = "Bench cannot be negative")
    private int bench;

    @Min(value = 0, message = "Squat cannot be negative")
    private int squat;

    @Min(value = 0, message = "Deadlift cannot be negative")
    private int deadlift;

    @Min(value = 0, message = "Total cannot be negative")
    private int total;


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

    public String getMembershipDate() {
        return membershipDate;
    }

    public void setMembershipDate(String membershipDate) {
        this.membershipDate = membershipDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public int getBench() {
        return bench;
    }

    public void setBench(int bench) {
        this.bench = bench;
    }

    public int getSquat() {
        return squat;
    }

    public void setSquat(int squat) {
        this.squat = squat;
    }

    public int getDeadlift() {
        return deadlift;
    }

    public void setDeadlift(int deadlift) {
        this.deadlift = deadlift;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public MemberRequestDTO(String name, LocalDate dateOfBirth, String membershipDate, String email,  Roles role, int bench, int squat, int deadlift, int total) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.membershipDate = membershipDate;
        this.role = role;
        this.email = email;
        this.bench = bench;
        this.squat = squat;
        this.deadlift = deadlift;
        this.total = total;
    }
}
