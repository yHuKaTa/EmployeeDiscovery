package com.sirma.exam.dtos;

import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
public class FireEmployeeRequest {
    @Pattern(regexp = "[\\d]+", message = "Employee ID must contain only digits")
    private Long id;

    @Pattern(regexp = "[\\d]{8,}", message = "Passport ID must contain at least 8 numeric symbols")
    private String passportId;

    public FireEmployeeRequest(Long id, String passportId) {
        this.id = id;
        this.passportId = passportId;
    }

    public Long getId() {
        return id;
    }

    public String getPassportId() {
        return passportId;
    }
}
