package com.sirma.exam.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
public class EditEmployeeRequest {
    @NotBlank(message = "First name must contain at least 4 alphabetic symbols")
    @Pattern(regexp = "[A-Za-z]{4,}", message = "First name must contain at least 4 alphabetic symbols")
    private String firstName;

    @NotBlank(message = "Last name must contain at least 4 alphabetic symbols")
    @Pattern(regexp = "[A-Za-z]{4,}", message = "Last name must contain at least 4 alphabetic symbols")
    private String lastName;

    public EditEmployeeRequest() {}

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
