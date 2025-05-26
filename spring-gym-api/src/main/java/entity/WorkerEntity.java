package entity;

import entity.enums.Roles;
import jakarta.persistence.*;
import org.hibernate.jdbc.Work;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "workers")
public class WorkerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private Roles role;

    @Transient
    private int age;

    public WorkerEntity(Long id, String name, LocalDate dateOfBirth, Roles role) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
    }
    public WorkerEntity(String name, LocalDate dateOfBirth, Roles role) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
    }
    public WorkerEntity () {};

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
}
