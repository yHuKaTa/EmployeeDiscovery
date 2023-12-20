package com.sirma.exam.services.impl;

import com.sirma.exam.converters.JobConverter;
import com.sirma.exam.dtos.AddJobToEmployeeRequest;
import com.sirma.exam.dtos.JobResponse;
import com.sirma.exam.models.Job;
import com.sirma.exam.repositories.JobRepository;
import com.sirma.exam.services.EmployeeService;
import com.sirma.exam.services.JobService;
import com.sirma.exam.services.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JobServiceImpl implements JobService {
    private EmployeeService employeeService;
    private ProjectService projectService;
    private JobRepository repository;

    @Autowired
    public JobServiceImpl(EmployeeService employeeService, ProjectService projectService, JobRepository repository) {
        this.employeeService = employeeService;
        this.projectService = projectService;
        this.repository = repository;
    }


    @Override
    public JobResponse getById(Long id) {
        Optional<Job> job = repository.findById(id);
        if (job.isPresent()) {
            Job searchedJob = job.get();
            return JobConverter.toResponse(searchedJob);
        } else throw new EntityNotFoundException("Job not found!");
    }

    @Override
    public List<JobResponse> getJobsByEmployeeId(Long id) {
        if (repository.count() <= 0) {
            throw new EntityNotFoundException("No jobs at this moment!");
        } else {
            List<Job> jobs = repository.findAllByEmployeeId(id);
            if (Objects.isNull(jobs) || jobs.isEmpty()) {
                throw new EntityNotFoundException("No jobs at this moment!");
            }
            List<JobResponse> responses = new LinkedList<>();
            for (Job job : jobs) {
                JobResponse response = JobConverter.toResponse(job);
                responses.add(response);
            }
            return responses;
        }
    }

    @Override
    public List<JobResponse> getJobsByProjectId(Long id) {
        return null;
    }

    @Override
    public JobResponse saveNewJob(AddJobToEmployeeRequest job) {
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }

    @Override
    public JobResponse editJobById(Long id) {
        return null;
    }
}
