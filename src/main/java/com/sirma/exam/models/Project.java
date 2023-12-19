package com.sirma.exam.models;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "project_name", unique = true)
    private String projectName;

    @Column(name = "start_date", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE,pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(name = "end_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE,pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ManyToMany
    @JoinTable(name = "project_job", joinColumns = {
            @JoinColumn(name = "project_id", referencedColumnName = "id")
    }, inverseJoinColumns = {
            @JoinColumn(name = "job_id", referencedColumnName = "id")
    })
    private Set<Job> jobs;

    public Project() {}

    public Project(String projectName, LocalDate startDate, LocalDate endDate, Set<Job> jobs) {
        this.projectName = projectName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.jobs = jobs;
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
        if (Objects.isNull(endDate)) {
            return LocalDate.now();
        } else {
            return endDate;
        }
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<Job> getJobs() {
        return jobs;
    }

    public void setJobs(Set<Job> jobs) {
        this.jobs = jobs;
    }
}
