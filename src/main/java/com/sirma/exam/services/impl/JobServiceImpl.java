package com.sirma.exam.services.impl;

import com.sirma.exam.converters.JobConverter;
import com.sirma.exam.dtos.AddJobToEmployeeRequest;
import com.sirma.exam.dtos.JobResponse;
import com.sirma.exam.models.Employee;
import com.sirma.exam.models.Job;
import com.sirma.exam.models.Project;
import com.sirma.exam.repositories.JobRepository;
import com.sirma.exam.services.EmployeeService;
import com.sirma.exam.services.JobService;
import com.sirma.exam.services.ProjectService;
import com.sirma.exam.utils.DateValidator;
import com.sirma.exam.utils.StringToDate;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
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
        if (employeeService instanceof EmployeeServiceImpl service && service.existsById(id)) {
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
        } else {
            // Information of the exception is incorrect for security propose!
            throw new EntityNotFoundException("No jobs at this moment!");
        }
    }

    @Override
    public List<JobResponse> getJobsByProjectId(Long id) {
        if (projectService instanceof ProjectServiceImpl service && service.existsById(id)) {
            if (repository.count() <= 0) {
                throw new EntityNotFoundException("No jobs at this moment!");
            } else {
                List<Job> jobs = repository.findAllByProjectId(id);
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
        } else throw new EntityNotFoundException("No jobs at this moment!");
    }

    @Override
    public JobResponse saveNewJob(AddJobToEmployeeRequest job, String passportId) {
        LocalDate startDate = StringToDate.toLocalDate(job.getStartDate());
        LocalDate endDate = StringToDate.toLocalDate(job.getEndDate());
        if (DateValidator.isDatesValid(startDate, endDate)
         && employeeService instanceof EmployeeServiceImpl currentEmployeeService
         && projectService instanceof ProjectServiceImpl currentProjectService) {
            Project project = null;
            if (currentProjectService.existsById(job.getProjectId())) {
                project = currentProjectService.getProjectById(job.getProjectId(), passportId);
                if (DateValidator.isSearchedDateValid(startDate, project.getStartDate(), project.getEndDate()) &&
                        DateValidator.isSearchedDateValid(endDate, project.getStartDate(), project.getEndDate())) {
                    Employee employee = null;
                    if (currentEmployeeService.existsById(job.getEmployeeId())) {
                        employee = currentEmployeeService.getEmployeeByPassportId(passportId);
                        return JobConverter.toResponse(repository.save(new Job(employee, project, startDate, endDate)));
                    } else throw new EntityNotFoundException("Employee with this ID does not exists!");
                } else throw new IllegalArgumentException("Provide valid start and end date of the job according to the project!");
            } else throw new EntityNotFoundException("Unable to receive information!");
        } else throw new IllegalArgumentException("Provide valid start and end date!");
    }

    @Override
    public boolean deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public JobResponse editJobById(Long id, String description) {
        if (repository.existsById(id)) {
            repository.updateDescriptionById(description, id);
            return getById(id);
        } else {
            return null;
        }
    }
}
