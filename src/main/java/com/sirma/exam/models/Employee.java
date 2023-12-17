package com.sirma.exam.models;

import jakarta.persistence.*;

import java.util.HashSet;
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

    @ManyToMany
    @JoinTable(name = "employee_project", joinColumns = {
            @JoinColumn(name = "employee_id", referencedColumnName = "id")
            }, inverseJoinColumns = {
                    @JoinColumn(name = "project_id", referencedColumnName = "id")
    })
    private Set<Project> projects;

    public Employee() {
        this.projects = new HashSet<>();
    }

    public Employee(String firstName, String lastName, String passportId, Set<Project> projects) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportId = passportId;
        this.projects = projects;
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

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }
}
