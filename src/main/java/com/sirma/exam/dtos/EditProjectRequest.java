package com.sirma.exam.dtos;

import com.sirma.exam.utils.annotations.ValidDateFormat;
import jakarta.validation.constraints.NotBlank;

public class EditProjectRequest {

    @NotBlank
    @ValidDateFormat
    private String endDate;

    public EditProjectRequest(String projectName, String endDate) {
        this.endDate = endDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
