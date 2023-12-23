package com.sirma.exam.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "passport_id", unique = true)
    private String passportId;

    @Column(name = "is_fired")
    private boolean isFired;

    @ManyToMany(mappedBy = "employee")
    @JsonBackReference
    private Set<Job> jobs;

    public Employee() {}

    public Employee(String firstName, String lastName, String passportId, boolean isFired, Set<Job> jobs) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportId = passportId;
        this.isFired = isFired;
        this.jobs = jobs;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public boolean isFired() {
        return isFired;
    }

    public void setFired() {
        isFired = true;
    }

    public Set<Job> getJobs() {
        return jobs;
    }

    public void setJobs(Set<Job> jobs) {
        this.jobs = jobs;
    }
}
