package dto;

import entity.CoachEntity;
import java.time.LocalDate;

public record CoachDTO(String name, LocalDate dateOfBirth, int age) {}
