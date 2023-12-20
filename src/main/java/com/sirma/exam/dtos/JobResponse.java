package com.sirma.exam.dtos;

public class JobResponse {
    private String description;
    private String projectName;
    private String employeeName;
    private String startDate;
    private String endDate;

    public JobResponse(String description, String projectName, String employeeName, String startDate, String endDate) {
        this.description = description;
        this.projectName = projectName;
        this.employeeName = employeeName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
