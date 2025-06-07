package practice.spring_gym_api.dto;

import java.time.LocalDate;

public class WorkerDTO {
    private String name;
    private int age;
    private LocalDate dateOfBirth;

    public WorkerDTO() {}

    public WorkerDTO(String name, int age, LocalDate dateOfBirth) {
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
