package practice.spring_gym_api.dto;

import practice.spring_gym_api.entity.enums.Roles;

import java.time.LocalDate;

public class MemberDTO {
    private String name;
    private int age;
    private LocalDate dateOfBirth;
    private Roles role;

    public MemberDTO(){}

    public MemberDTO(String name, int age, LocalDate dateOfBirth){
        this.name = name;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
