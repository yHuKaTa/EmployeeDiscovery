package com.sirma.exam.services.impl;

import com.sirma.exam.converters.ProjectConverter;
import com.sirma.exam.dtos.AddNewProjectRequest;
import com.sirma.exam.dtos.EditProjectRequest;
import com.sirma.exam.dtos.ProjectResponse;
import com.sirma.exam.models.Project;
import com.sirma.exam.repositories.ProjectRepository;
import com.sirma.exam.services.EmployeeService;
import com.sirma.exam.services.ProjectService;
import com.sirma.exam.utils.DateValidator;
import com.sirma.exam.utils.StringToDate;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class ProjectServiceImpl implements ProjectService {
    private ProjectRepository repository;

    private EmployeeService employeeService;

    @Autowired
    public ProjectServiceImpl(ProjectRepository repository, EmployeeService employeeService) {
        this.repository = repository;
        this.employeeService = employeeService;
    }

    @Override
    public ProjectResponse getById(Long id) {
        Optional<Project> project = repository.findById(id);
        if (project.isPresent()) {
            Project searchedProject = project.get();
            return ProjectConverter.toResponse(searchedProject);
        } else throw new EntityNotFoundException("Project not found!");
    }

    @Override
    public Project getProjectById(Long id, String passportId) {
        if (Objects.isNull(employeeService.getEmployeeByPassportId(passportId))) {
            throw new IllegalAccessError("Unable to receive information!");
        }
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Project not found!"));
    }

    @Override
    public List<ProjectResponse> getAll() {
        if (repository.count() <= 0) {
            throw new EntityNotFoundException("No projects at this moment.");
        } else {
            List<Project> projects = repository.findAll();
            List<ProjectResponse> responses = new ArrayList<>();
            for (Project project : projects) {
                ProjectResponse response = ProjectConverter.toResponse(project);
                responses.add(response);
            }
            return responses;
        }
    }

    @Override
    public ProjectResponse saveNewProject(AddNewProjectRequest request) {
        if (repository.doesNotExistsByProjectName(request.getProjectName())) {
            Project project = repository.save(ProjectConverter.toNewProject(request));
            return getById(project.getId());
        } else {
            // We must provide information about the problem that does not identify the specific problem in order to protect from information leakage
            throw new EntityExistsException("Failed to create project! Contact technical support for more information.");
        }
    }

    @Override
    public boolean deleteProjectById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public ProjectResponse editProjectById(Long id, EditProjectRequest request, String passportId) {
        if (repository.existsById(id)) {
            Project project = getProjectById(id, passportId);
            LocalDate editedDate = StringToDate.toLocalDate(request.getEndDate());
            if (DateValidator.isEditedEndDateValid(editedDate, project.getStartDate(), project.getEndDate())) {
                repository.editProject(editedDate, id);
            } else throw new IllegalArgumentException("Provided new end date is invalid!");
            return ProjectConverter.toResponse(getProjectById(id, passportId));
        } else {
            return null;
        }
    }
}
