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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Set<Job> getJobsList() {
        return jobsList;
    }
}
