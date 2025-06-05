package entity;
import entity.enums.Roles;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "members")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private Long id;
   private String name;
   private LocalDate dateOfBirth;

   @ManyToOne
   @JoinColumn(name = "coach_id")
   private CoachEntity coachedBy;
   private String membershipDate;
   private Roles role;
   @Transient
   private int age;

   private int bench;
   private int squat;
   private int deadlift;
   private int total;

    public MemberEntity(String name, LocalDate dateOfBirth, String membershipDate, Roles role, int bench, int squat, int deadlift, int total) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.membershipDate = membershipDate;
        this.role = role;
        this.bench = bench;
        this.squat = squat;
        this.deadlift = deadlift;
        this.total = total;
    }
    public MemberEntity(String name) {
        this.name = name;
    }


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
}
