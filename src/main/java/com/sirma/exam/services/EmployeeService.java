package com.sirma.exam.services;

import com.sirma.exam.dtos.AddEmployeeRequest;
import com.sirma.exam.dtos.EditEmployeeRequest;
import com.sirma.exam.dtos.EmployeeResponse;
import com.sirma.exam.dtos.FireEmployeeRequest;
import com.sirma.exam.models.Employee;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse getById(Long id);

    Employee getEmployeeByPassportId(String passportId);

    List<EmployeeResponse> getAll();

    EmployeeResponse saveNewEmployee(AddEmployeeRequest request);

    EmployeeResponse fireEmployee(Long id, FireEmployeeRequest request);

    boolean deleteEmployeeById(Long id);

    EmployeeResponse editEmployeeByPassportId(String passportId, EditEmployeeRequest request);
}
