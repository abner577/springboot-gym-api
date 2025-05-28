package dto;

import java.time.LocalDate;

public record WorkerDTO(String name, LocalDate dateOfBirth, int age) {
}
