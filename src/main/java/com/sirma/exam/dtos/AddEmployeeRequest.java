package com.sirma.exam.dtos;

import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
public class AddEmployeeRequest {
    @Pattern(regexp = "[A-Za-z]{4,}", message = "First name must contain at least 4 alphabetic symbols")
    private String firstName;

    @Pattern(regexp = "[A-Za-z]{4,}", message = "Last name must contain at least 4 alphabetic symbols")
    private String lastName;

    @Pattern(regexp = "[\\d]{8,}", message = "Passport ID must contain at least 8 numeric symbols")
    private String passportId;

    public AddEmployeeRequest(String firstName, String lastName, String passportId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportId = passportId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassportId() {
        return passportId;
    }
}
