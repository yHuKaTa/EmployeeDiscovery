package com.sirma.exam.dtos;

import com.sirma.exam.utils.annotations.ValidDateFormat;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
public class AddNewProjectRequest {
    @Pattern(regexp = "[\\a]{8,}", message = "Project Name must contains at least 8 symbols")
    private String projectName;

    @ValidDateFormat
    private String startDate;

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
