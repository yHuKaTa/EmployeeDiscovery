package com.sirma.exam.controllers;

import com.sirma.exam.dtos.*;
import com.sirma.exam.models.Project;
import com.sirma.exam.services.ProjectService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/projects")
@Validated
public class ProjectController {
    private ProjectService service;

    @Autowired
    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable @Pattern(regexp = "[\\d]", message = "Project ID must contain only digits") @Valid Long id) {
        ProjectResponse response = service.getById(id);
        if (Objects.isNull(response)) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/{id}/get")
    public ResponseEntity<Project> getProjectById(@PathVariable @Pattern(regexp = "[\\d]", message = "Project ID must contain only digits") @Valid Long id,
            @RequestHeader(name = "passportId") @Pattern(regexp = "[\\d]{8,}", message = "Passport ID must contain at least 8 digits") @Valid String passportId) {
        Project project = service.getProjectById(id, passportId);
        if (Objects.nonNull(project)) {
            return ResponseEntity.ok(project);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        List<ProjectResponse> projects = service.getAll();
        if (Objects.isNull(projects) || projects.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(projects);
        }
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> saveNewProject(@RequestBody @Valid AddNewProjectRequest request) {
        ProjectResponse response = service.saveNewProject(request);
        if (Objects.isNull(response)) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable @Pattern(regexp = "[\\d]", message = "Project ID must contain only digits") @Valid Long id) {
        if (service.deleteProjectById(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> editProjectById(@Pattern(regexp = "[\\d]", message = "Project ID must contain only digits") @Valid Long id,
                                                             @RequestBody @Valid EditProjectRequest request,
                                                           @RequestHeader("passportId") @Pattern(regexp = "[\\d]{8,}", message = "Passport ID must contain at least 8 digits") @Valid String passportId) {
        ProjectResponse response = service.editProjectById(id, request, passportId);
        if (Objects.isNull(response)) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(response);
        }
    }
}
