package com.sirma.exam.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
public class AddEmployeeRequest {
    @NotBlank(message = "First name must contain at least 4 alphabetic symbols")
    @Pattern(regexp = "[A-Za-z]{4,}", message = "First name must contain at least 4 alphabetic symbols")
    private String firstName;

    @NotBlank(message = "Last name must contain at least 4 alphabetic symbols")
    @Pattern(regexp = "[A-Za-z]{4,}", message = "Last name must contain at least 4 alphabetic symbols")
    private String lastName;

    @NotBlank(message = "Passport ID must contain at least 8 numeric symbols")
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
