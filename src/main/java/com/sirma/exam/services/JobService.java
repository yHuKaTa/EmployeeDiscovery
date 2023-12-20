package com.sirma.exam.services;

import com.sirma.exam.dtos.AddJobToEmployeeRequest;
import com.sirma.exam.dtos.JobResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface JobService {
    JobResponse getById(Long id);
    List<JobResponse> getJobsByEmployeeId(Long id);
    List<JobResponse> getJobsByProjectId(Long id);
    JobResponse saveNewJob(AddJobToEmployeeRequest job, String passportId);
    boolean deleteById(Long id);
    JobResponse editJobById(Long id, String description);
}
