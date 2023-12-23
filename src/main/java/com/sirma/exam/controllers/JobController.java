package com.sirma.exam.controllers;

import com.sirma.exam.dtos.AddJobToEmployeeRequest;
import com.sirma.exam.dtos.EmployeeResponse;
import com.sirma.exam.dtos.JobResponse;
import com.sirma.exam.services.JobService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/jobs")
@Validated
public class JobController {
    private final JobService service;

    @Autowired
    public JobController(JobService service) {
        this.service = service;
    }

    @GetMapping(path = "/topPair")
    public ResponseEntity<List<EmployeeResponse>> getTopEmployees() {
        List<EmployeeResponse> response = service.findPairOfEmployee();
        if (Objects.isNull(response) || response.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping(path = "/topPair/jobs")
    public ResponseEntity<List<JobResponse>> getJobsOfTopEmployees() {
        List<JobResponse> responses = service.findAllProjectsForEmployees();
        if (Objects.isNull(responses) || responses.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(responses);
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable @Digits(integer = 19, fraction = 0, message = "Job ID must contain only digits!") @Valid Long id) {
        JobResponse response = service.getById(id);
        if (Objects.isNull(response)) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<List<JobResponse>> getJobsByEmployeeId(@PathVariable @Digits(integer = 19, fraction = 0, message = "Employee ID must contain only digits!") @Valid Long id) {
        List<JobResponse> responses = service.getJobsByEmployeeId(id);
        if (Objects.isNull(responses) || responses.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(responses);
        }
    }

    @GetMapping("/project/{id}")
    public ResponseEntity<List<JobResponse>> getJobsByProjectId(@PathVariable @Digits(integer = 19, fraction = 0, message = "Project ID must contain only digits!") @Valid Long id) {
        List<JobResponse> responses = service.getJobsByProjectId(id);
        if (Objects.isNull(responses) || responses.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(responses);
        }
    }

    @PostMapping
    public ResponseEntity<JobResponse> saveNewJob(@RequestHeader("passportId") @Pattern(regexp = "[\\d]{8,}", message = "Passport ID must contain at least 8 digits") @Valid String passportId,
                                                  @RequestBody @Valid AddJobToEmployeeRequest request) {
        JobResponse response = service.saveNewJob(request, passportId);
        if (Objects.isNull(response)) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable @Digits(integer = 19, fraction = 0, message = "Job ID must contain only digits!") @Valid Long id) {
        if (service.deleteById(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobResponse> editJob(@PathVariable @Digits(integer = 19, fraction = 0, message = "Job ID must contain only digits!") @Valid Long id,
                                               @RequestBody @NotBlank @Pattern(regexp = "(([\\w])+|([\\s])*([\\p{Punct}]?))*", message = "Description must contain alphanumeric text with punctuations.") @Valid String description) {
        JobResponse response = service.editJobById(id, description);
        if (Objects.isNull(response)) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(response);
        }
    }
}
