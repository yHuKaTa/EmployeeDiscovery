package com.sirma.exam.dtos;

import java.util.Set;

public class EmployeeResponse {
    private String firstName;
    private String lastName;
    private Set<JobResponse> jobsList;

    public EmployeeResponse(String firstName, String lastName, Set<JobResponse> jobsList) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobsList = jobsList;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Set<JobResponse> getJobsList() {
        return jobsList;
    }
}
