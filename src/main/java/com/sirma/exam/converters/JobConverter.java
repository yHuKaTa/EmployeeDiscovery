package com.sirma.exam.converters;

import com.sirma.exam.dtos.JobResponse;
import com.sirma.exam.models.Job;

public class JobConverter {
    public static JobResponse toResponse(Job job) {
        return new JobResponse(job.getDescription(),
                job.getProject().getProjectName(),
                job.getEmployee().getFirstName() + " " + job.getEmployee().getLastName(),
                job.getStartDate().toString(),
                job.getEndDate().toString());
    }
}
