package dto;

import java.time.LocalDate;

public record MemberDTO(String name, LocalDate dateOfBirth, int age) {}
