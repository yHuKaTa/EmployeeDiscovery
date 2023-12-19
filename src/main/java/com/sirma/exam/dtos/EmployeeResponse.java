package com.sirma.exam.dtos;

import com.sirma.exam.models.Job;

import java.util.Set;

public class EmployeeResponse {
    private String firstName;
    private String lastName;
    private Set<Job> jobsList;

    public EmployeeResponse(String firstName, String lastName, Set<Job> jobsList) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobsList = jobsList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Employee: ").append(firstName).append(" ").append(lastName).append(" is working in projects:\n");
        for (Job project : jobsList) {
            builder.append(project.getProject().getProjectName()).append(" from: ").append(project.getStartDate().toString()).append(" to: ").append(project.getEndDate().toString()).append("\n");
        }
        return builder.toString();
    }
}
