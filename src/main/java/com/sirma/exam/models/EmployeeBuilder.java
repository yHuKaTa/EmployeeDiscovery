package com.sirma.exam.models;

import java.util.Objects;
import java.util.Set;

public class EmployeeBuilder {
    private static EmployeeBuilder employeeBuilder;
    private String firstName;
    private String lastName;
    private String passportId;
    private Set<Project> projects;

    public EmployeeBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public EmployeeBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public EmployeeBuilder passportId(String passportId) {
        this.passportId = passportId;
        return this;
    }

    public EmployeeBuilder projects(Set<Project> projects) {
        this.projects = projects;
        return this;
    }

    public Employee build() {
        return new Employee(firstName, lastName, passportId, projects);
    }

    public static EmployeeBuilder builder() {
        if (Objects.isNull(employeeBuilder)) {
            employeeBuilder = new EmployeeBuilder();
        }
        return employeeBuilder;
    }
}
