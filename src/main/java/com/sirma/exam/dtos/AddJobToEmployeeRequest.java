package com.sirma.exam.dtos;

import com.sirma.exam.utils.annotations.ValidDateFormat;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
public class AddJobToEmployeeRequest {
    @NotBlank(message = "Description must contain alphanumeric text with punctuations.")
    @Pattern(regexp = "(([\\w])+|([\\s])*([\\p{Punct}]?))*", message = "Description must contain alphanumeric text with punctuations.")
    private String description;
    @Digits(integer = 19, fraction = 0, message = "Job ID must contain only digits!")
    private Long employeeId;

    @Digits(integer = 19, fraction = 0, message = "Job ID must contain only digits!")
    private Long projectId;

    @NotBlank(message = "Start date of job must be not blank")
    @ValidDateFormat
    private String startDate;

    @Nullable
    @ValidDateFormat
    private String endDate;

    public AddJobToEmployeeRequest(String description, Long employeeId, Long projectId, String startDate, String endDate) {
        this.description = description;
        this.projectId = projectId;
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
