package com.sirma.exam.dtos;

import com.sirma.exam.utils.annotations.ValidDateFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
public class AddJobToEmployeeRequest {
    @NotBlank
    @Pattern(regexp = "[\\w\\p{Punct}]", message = "Description must contain alphanumeric text with punctuations.")
    private String description;
    @Pattern(regexp = "[\\d]", message = "Employee ID must contain only digits")
    private String employeeId;

    @Pattern(regexp = "[\\d]", message = "Project ID must contains only digits")
    private String projectId;

    @ValidDateFormat
    private String startDate;

    @ValidDateFormat
    private String endDate;

    public AddJobToEmployeeRequest(String description, String employeeId, String projectId, String startDate, String endDate) {
        this.description = description;
        this.projectId = projectId;
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
