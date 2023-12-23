package com.sirma.exam.converters;

import com.sirma.exam.dtos.AddEmployeeRequest;
import com.sirma.exam.dtos.EmployeeResponse;
import com.sirma.exam.dtos.JobResponse;
import com.sirma.exam.models.Employee;
import com.sirma.exam.models.Job;

import java.util.HashSet;
import java.util.Set;

public class EmployeeConverter {
    public static Employee toNewEmployee(AddEmployeeRequest request) {
        return new Employee(request.getFirstName(), request.getLastName(), request.getPassportId(), false, new HashSet<>());
    }

    public static EmployeeResponse toResponse(Employee employee) {
        Set<JobResponse> responses = new HashSet<>();
        for (Job job : employee.getJobs()) {
            responses.add(JobConverter.toResponse(job));
        }
        return new EmployeeResponse(employee.getFirstName(), employee.getLastName(), responses);
    }
}
