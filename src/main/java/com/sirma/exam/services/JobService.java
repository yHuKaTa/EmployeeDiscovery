package com.sirma.exam.services;

import com.sirma.exam.dtos.AddJobToEmployeeRequest;
import com.sirma.exam.dtos.EmployeeResponse;
import com.sirma.exam.dtos.JobResponse;
import com.sirma.exam.dtos.ProjectResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface JobService {
    JobResponse getById(Long id);
    List<JobResponse> getJobsByEmployeeId(Long id);
    List<JobResponse> getJobsByProjectId(Long id);
    JobResponse saveNewJob(AddJobToEmployeeRequest job, String passportId);
    boolean deleteById(Long id);
    JobResponse editJobById(Long id, String description);
    List<EmployeeResponse> findPairOfEmployee();
    List<JobResponse> findAllProjectsForEmployees();
}
