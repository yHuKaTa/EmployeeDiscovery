package com.sirma.exam.dtos;

import com.sirma.exam.utils.annotations.ValidDateFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
public class AddNewProjectRequest {
    @NotBlank(message = "Project name must contain at least 8 symbols")
    @Pattern(regexp = "[\\w]{8,}", message = "Project Name must contains at least 8 symbols")
    private String projectName;

    @NotBlank(message = "Start date of project must be not blank")
    @ValidDateFormat
    private String startDate;

    @NotBlank(message = "End date of project must be not blank")
    @ValidDateFormat
    private String endDate;

    public AddNewProjectRequest(String projectName, String startDate, String endDate) {
        this.projectName = projectName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
