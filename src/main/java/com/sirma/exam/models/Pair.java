package com.sirma.exam.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
@Table(name = "pairs")
public class Pair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "other_employee_id", nullable = false)
    private Long otherEmployeeId;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    public Pair() {
    }

    public Pair(Long employeeId, Long otherEmployeeId, Long projectId, LocalDate startDate, LocalDate endDate) {
        this.employeeId = employeeId;
        this.otherEmployeeId = otherEmployeeId;
        this.projectId = projectId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public Long getOtherEmployeeId() {
        return otherEmployeeId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Long getTimeStamp() {
        if (Objects.isNull(endDate)) {
            return ChronoUnit.DAYS.between(startDate, LocalDate.now());
        }
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
}
