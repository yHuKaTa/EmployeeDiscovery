package com.sirma.exam.dtos;

import com.sirma.exam.utils.annotations.ValidDateFormat;

public class EditProjectRequest {
    @ValidDateFormat
    private String startDate;

    @ValidDateFormat
    private String endDate;

    public EditProjectRequest(String projectName, String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
