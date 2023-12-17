package com.sirma.exam.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToMany
    @JoinTable(name = "employee_project", joinColumns = {
            @JoinColumn(name = "project_id", referencedColumnName = "id")
    }, inverseJoinColumns = {
            @JoinColumn(name = "employee_id", referencedColumnName = "id")
    })
    private Set<Employee> employees;

    public Project() {
        this.employees = new HashSet<>();
    }

    public Project(String projectName, LocalDate startDate, LocalDate endDate, Set<Employee> employees) {
        this.projectName = projectName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.employees = employees;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
}
