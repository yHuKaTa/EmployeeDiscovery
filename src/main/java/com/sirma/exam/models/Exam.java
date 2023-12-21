package com.sirma.exam.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
@Table(name = "Exam")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "employee_id", nullable = false)
    private Long projectId;

    @Column(name = "employee_id", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-yy")
    private LocalDate startDate;

    @Column(name = "employee_id")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-yy")
    private LocalDate endDate;

    public Exam() {}

    public Exam(Long employeeId, Long projectId, LocalDate startDate, LocalDate endDate) {
        this.employeeId = employeeId;
        this.projectId = projectId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        if (Objects.isNull(endDate)) {
            return LocalDate.now();
        }
        return endDate;
    }

    public Long getDays() {
        if (Objects.isNull(endDate)) {
            return ChronoUnit.DAYS.between(startDate, LocalDate.now());
        }
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
}
