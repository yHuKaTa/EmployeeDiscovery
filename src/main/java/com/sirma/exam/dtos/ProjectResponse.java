package com.sirma.exam.dtos;


public class ProjectResponse {
    private String projectName;
    private String startDate;
    private String endDate;

    public ProjectResponse(String projectName, String startDate, String endDate) {
        this.projectName = projectName;
        this.startDate = startDate;
        this.endDate = endDate;
    }


}
