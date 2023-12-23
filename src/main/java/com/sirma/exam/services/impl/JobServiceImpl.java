package com.sirma.exam.services.impl;

import com.sirma.exam.converters.EmployeeConverter;
import com.sirma.exam.converters.JobConverter;
import com.sirma.exam.converters.ProjectConverter;
import com.sirma.exam.dtos.AddJobToEmployeeRequest;
import com.sirma.exam.dtos.EmployeeResponse;
import com.sirma.exam.dtos.JobResponse;
import com.sirma.exam.dtos.ProjectResponse;
import com.sirma.exam.models.Employee;
import com.sirma.exam.models.Exam;
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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
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

    Set<Job> jobs() {
        return Set.copyOf(repository.findAll());
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
                        if (employee.isFired()) {
                            throw new EntityNotFoundException("Employee with this ID does not exists!");
                        }
                        return JobConverter.toResponse(repository.save(new Job(job.getDescription(), employee, project, startDate, endDate)));
                    } else throw new EntityNotFoundException("Employee with this ID does not exists!");
                } else
                    throw new IllegalArgumentException("Provide valid start and end date of the job according to the project!");
            } else throw new EntityNotFoundException("Unable to receive information! Contact technical support for more details.");
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

    @Override
    public List<EmployeeResponse> findPairOfEmployee() {
        List<EmployeeResponse> responses = new ArrayList<>();
        List<Employee> topEmployees = getTopEmployees();
        if (Objects.nonNull(topEmployees)) {
            for (Employee employee : topEmployees) {
                responses.add(EmployeeConverter.toResponse(employee));
            }
            return responses;
        } else {
            return null;
        }
    }

    @Override
    public List<JobResponse> findAllProjectsForEmployees() {
        List<JobResponse> responses = new ArrayList<>();
        List<Employee> topEmployees = getTopEmployees();
        if (Objects.nonNull(topEmployees)) {
            for (Employee employee : topEmployees) {
                for (Job job : employee.getJobs()) {
                    responses.add(JobConverter.toResponse(job));
                }
            }
            return responses;
        } else {
            return null;
        }
    }



    private List<Employee> getTopEmployees() {
        List<Employee> employees;
        if (employeeService instanceof EmployeeServiceImpl currentEmployeeService) {
            employees = currentEmployeeService.employees();
        } else {
            employees = null;
        }
        List<Employee> pairEmployees;
        if (Objects.nonNull(employees) && !employees.isEmpty() && employees.size() > 1) {
            pairEmployees = employees.stream()
                    .flatMap(employee -> employees.stream()
                            .filter(otherEmployee -> !(employee.getId().equals(otherEmployee.getId())))
                            .flatMap(otherEmployee -> employee.getJobs().stream()
                                    .flatMap(job -> otherEmployee.getJobs().stream()
                                            .filter(otherJob -> job.getProject().getId().equals(otherJob.getProject().getId()) && recordsOverlap(job, otherJob))
                                            .map(otherJob -> List.of(employee, otherEmployee))
                                    )
                            )
                    )
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        if (pairEmployees.size() < 2) {
            return null;
        }
            return pairEmployees.stream()
                    .distinct()
                    .sorted(Comparator.comparingLong(employee ->
                            employee.getJobs().stream()
                                    .mapToLong(job -> ChronoUnit.DAYS.between(job.getStartDate(), job.getEndDate()))
                                    .sum())).limit(2).collect(Collectors.toList());
        } else {
            return null;
        }
    }

    private boolean recordsOverlap(Job record1, Job record2) {
        if (record1.getEndDate().isBefore(record2.getStartDate())) {
            return false;
        }
        if (record2.getEndDate().isBefore(record1.getStartDate())) {
            return false;
        }
        if (isPeriodGraterThen(record1, record2)) {
            return (record1.getStartDate().isBefore(record2.getStartDate()) ||
                    record1.getStartDate().isEqual(record2.getStartDate()));
        } else {
            return (record2.getStartDate().isBefore(record1.getStartDate()) ||
                    record2.getStartDate().isEqual(record1.getStartDate()));
        }
    }

    private boolean isPeriodGraterThen(Job record1, Job record2) {
        long time1 = ChronoUnit.DAYS.between(record1.getStartDate(), record1.getEndDate());
        long time2 = ChronoUnit.DAYS.between(record2.getStartDate(), record2.getEndDate());
        return  (time1 >= time2);
    }
}

