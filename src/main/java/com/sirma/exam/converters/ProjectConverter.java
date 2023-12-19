package com.sirma.exam.converters;

import com.sirma.exam.dtos.AddNewProjectRequest;
import com.sirma.exam.dtos.ProjectResponse;
import com.sirma.exam.models.Project;
import com.sirma.exam.utils.StringToDate;

import java.util.HashSet;

public class ProjectConverter {
    public static Project toNewProject(AddNewProjectRequest request) {
        return new Project(
                request.getProjectName(),
                StringToDate.toLocalDate(request.getStartDate()),
                StringToDate.toLocalDate(request.getEndDate()),
                new HashSet<>());
    }

    public static ProjectResponse toResponse(Project project) {
        return new ProjectResponse(project.getProjectName(), project.getStartDate().toString(), project.getEndDate().toString());
    }
}
