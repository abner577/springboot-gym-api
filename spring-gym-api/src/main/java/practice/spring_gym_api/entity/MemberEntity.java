package practice.spring_gym_api.entity;
import jakarta.validation.constraints.*;
import practice.spring_gym_api.entity.enums.Roles;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "members")
public class MemberEntity {

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

    @NotBlank(message = "Email is Required")
    @Email(message = "Invalid email format")
    private String email;


   @ManyToOne
   @JoinColumn(name = "coach_id")
   @NotNull(message = "Coach is required")
   private CoachEntity coachedBy;

    @NotBlank(message = "Membership date is Required")
   private String membershipDate;

    @NotNull(message = "Role is Required")
    @Enumerated(EnumType.STRING)
   private Roles role;
   @Transient
   private int age;

    @Min(value = 1, message = "Bench must be greater than 0")
   private int bench;
    @Min(value = 1, message = "Squat must be greater than 0")
   private int squat;
    @Min(value = 1, message = "Deadlift must be greater than 0")
   private int deadlift;
    @Min(value = 1, message = "Total must be greater than 0")
   private int total;

    public MemberEntity(String name, LocalDate dateOfBirth, String membershipDate, String email,  Roles role, int bench, int squat, int deadlift, int total) {
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
    public MemberEntity(String name) {
        this.name = name;
    }
    public MemberEntity() {}


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

    public CoachEntity getCoachedBy() {
        return coachedBy;
    }

    public void setCoachedBy(CoachEntity coachedBy) {
        this.coachedBy = coachedBy;
    }

    public String getMembershipDate() {
        return membershipDate;
    }

    public void setMembershipDate(String membershipDate) {
        this.membershipDate = membershipDate;
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
        return this.bench + this.squat + this.deadlift;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}
}
