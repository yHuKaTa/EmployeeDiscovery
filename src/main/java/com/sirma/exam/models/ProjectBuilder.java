package com.sirma.exam.models;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

public class ProjectBuilder {
    private static ProjectBuilder projectBuilder;

    private String projectName;

    private LocalDate startDate;

    private LocalDate endDate;

    private Set<Employee> employees;

    private ProjectBuilder() {}

    public static ProjectBuilder builder() {
        if (Objects.isNull(projectBuilder)) {
            projectBuilder = new ProjectBuilder();
        }
        return projectBuilder;
    }

    public ProjectBuilder projectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public ProjectBuilder startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public ProjectBuilder endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public ProjectBuilder employees(Set<Employee> employees) {
        this.employees = employees;
        return this;
    }

    public Project build() {
        return new Project(this.projectName, this.startDate, this.endDate, this.employees);
    }
}
