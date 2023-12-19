package com.sirma.exam.converters;

import com.sirma.exam.dtos.AddEmployeeRequest;
import com.sirma.exam.dtos.EmployeeResponse;
import com.sirma.exam.models.Employee;

import java.util.HashSet;

public class EmployeeConverter {
    public static Employee toNewEmployee(AddEmployeeRequest request) {
        return new Employee(request.getFirstName(), request.getLastName(), request.getPassportId(), false, new HashSet<>());
    }

    public static EmployeeResponse toResponse(Employee employee) {
        return new EmployeeResponse(employee.getFirstName(), employee.getLastName(), employee.getJobs());
    }
}
