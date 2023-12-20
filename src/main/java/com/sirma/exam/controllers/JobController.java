package com.sirma.exam.controllers;

import com.sirma.exam.dtos.JobResponse;
import com.sirma.exam.services.JobService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/jobs")
@Validated
public class JobController {
    private JobService service;

    @Autowired
    public JobController(JobService service) {
        this.service = service;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable @Pattern(regexp = "[\\d]", message = "Job ID must contain only digits") @Valid Long id) {
        JobResponse response = service.getById(id);
        if (Objects.isNull(response)) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(response);
        }
    }
}
