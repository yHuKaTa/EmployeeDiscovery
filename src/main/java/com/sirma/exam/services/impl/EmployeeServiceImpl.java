package com.sirma.exam.services.impl;

import com.sirma.exam.converters.EmployeeConverter;
import com.sirma.exam.dtos.AddEmployeeRequest;
import com.sirma.exam.dtos.EditEmployeeRequest;
import com.sirma.exam.dtos.EmployeeResponse;
import com.sirma.exam.dtos.FireEmployeeRequest;
import com.sirma.exam.models.Employee;
import com.sirma.exam.repositories.EmployeeRepository;
import com.sirma.exam.services.EmployeeService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private EmployeeRepository repository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    boolean existsById(Long id) {
        return repository.existsById(id);
    }

    Set<Employee> employees() {
        return Set.copyOf(repository.findAll());
    }

    @Override
    public EmployeeResponse getById(Long id) {
        Optional<Employee> employee = repository.findById(id);
        if (employee.isPresent()) {
            Employee searchedEmployee = employee.get();
            return EmployeeConverter.toResponse(searchedEmployee);
        } else throw new EntityNotFoundException("Employee not found!");
    }

    @Override
    public Employee getEmployeeByPassportId(String passportId) {
        Optional<Employee> employee = repository.findByPassportId(passportId);
        return employee.orElseThrow(() -> new EntityNotFoundException("Employee not found!"));
    }

    @Override
    public List<EmployeeResponse> getAll() {
        if (repository.count() <= 0) {
            throw new EntityNotFoundException("No employees at this moment.");
        } else {
            List<Employee> employees = repository.findAll();
            List<EmployeeResponse> responses = new ArrayList<>();
            for (Employee employee : employees) {
                EmployeeResponse response = EmployeeConverter.toResponse(employee);
                responses.add(response);
            }
            return responses;
        }
    }

    @Override
    public EmployeeResponse saveNewEmployee(AddEmployeeRequest request) {
        if (!(repository.existsByPassportId(request.getPassportId()))) {
            Employee employee = repository.save(EmployeeConverter.toNewEmployee(request));
            return getById(employee.getId());
        } else {
            // In server are registered employee with stolen personal data!
            throw new EntityExistsException("Contact technical support for more information and to clarify the issue.");
        }
    }

    @Override
    public EmployeeResponse fireEmployee(Long id, FireEmployeeRequest request) {
        if (id.equals(request.getId()) && repository.existsById(request.getId()) && repository.existsByPassportId(request.getPassportId())) {
            repository.fireEmployee(true, request.getId(), request.getPassportId());
            return getById(id);
        } else throw new IllegalArgumentException("Invalid provided data!");
    }

    @Override
    public boolean deleteEmployeeById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public EmployeeResponse editEmployeeByPassportId(String passportId, EditEmployeeRequest request) {
        if (repository.existsByPassportId(passportId)) {
            repository.editEmployee(request.getFirstName(), request.getLastName(), passportId);
            return EmployeeConverter.toResponse(getEmployeeByPassportId(passportId));
        } else {
            // Not to give away personal data if this one exists or not
            throw new IllegalArgumentException("Invalid provided data!");
        }
    }
}
