package com.sirma.exam.converters;

import com.sirma.exam.dtos.AddNewProjectRequest;
import com.sirma.exam.dtos.ProjectResponse;
import com.sirma.exam.models.Project;
import com.sirma.exam.utils.DateValidator;
import com.sirma.exam.utils.StringToDate;

import java.time.LocalDate;
import java.util.HashSet;

public class ProjectConverter {
    public static Project toNewProject(AddNewProjectRequest request) {
        LocalDate startDate = StringToDate.toLocalDate(request.getStartDate());
        LocalDate endDate = StringToDate.toLocalDate(request.getEndDate());
        if (DateValidator.isDatesValid(startDate, endDate)) {
            return new Project(
                    request.getProjectName(),
                    startDate,
                    endDate,
                    new HashSet<>());
        } else throw new IllegalArgumentException("Provide valid start and end dates!");
    }

    public static ProjectResponse toResponse(Project project) {
        return new ProjectResponse(project.getProjectName(), project.getStartDate().toString(), project.getEndDate().toString());
    }
}
