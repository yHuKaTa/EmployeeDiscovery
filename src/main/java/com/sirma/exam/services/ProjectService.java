package com.sirma.exam.services;

import com.sirma.exam.dtos.AddNewProjectRequest;
import com.sirma.exam.dtos.EditProjectRequest;
import com.sirma.exam.dtos.ProjectResponse;
import com.sirma.exam.models.Project;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProjectService {
    ProjectResponse getById(Long id);

    Project getProjectById(Long id, String passportId);

    List<ProjectResponse> getAll();

    ProjectResponse saveNewProject(AddNewProjectRequest request);

    boolean deleteProjectById(Long id);

    ProjectResponse editProjectById(Long id, EditProjectRequest request, String passportId);
}
