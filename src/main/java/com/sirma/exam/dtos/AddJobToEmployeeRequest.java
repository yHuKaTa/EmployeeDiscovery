package com.sirma.exam.dtos;

import com.sirma.exam.utils.annotations.ValidDateFormat;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
public class AddJobToEmployeeRequest {
    @Pattern(regexp = "[\\d]", message = "Employee ID must contain only digits")
    public String employeeId;

    @Pattern(regexp = "[\\d]", message = "Project ID must contains only digits")
    private String projectId;

    @ValidDateFormat
    private String startDate;

    @ValidDateFormat
    private String endDate;

    public AddJobToEmployeeRequest(String employeeId, String projectId, String startDate, String endDate) {
        this.projectId = projectId;
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getEmployeeId() {
        return employeeId;
    }
}
