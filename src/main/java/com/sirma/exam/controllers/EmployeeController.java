package com.sirma.exam.controllers;

import com.sirma.exam.dtos.AddEmployeeRequest;
import com.sirma.exam.dtos.EditEmployeeRequest;
import com.sirma.exam.dtos.EmployeeResponse;
import com.sirma.exam.dtos.FireEmployeeRequest;
import com.sirma.exam.models.Employee;
import com.sirma.exam.services.EmployeeService;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/employees")
@Validated
public class EmployeeController {
    private EmployeeService service;

    @Autowired
    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable @Digits(integer = 19, fraction = 0, message = "Employee ID must contain only digits!") @Valid Long id) {
        EmployeeResponse response = service.getById(id);
        if (Objects.isNull(response)) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<Employee> getEmployeeByPassportId(@RequestHeader(name = "passportId") @Pattern(regexp = "[\\d]{8,}", message = "Passport ID must contain at least 8 digits!") @Valid String passportId) {
        Employee employee = service.getEmployeeByPassportId(passportId);
        if (Objects.nonNull(employee)) {
            return ResponseEntity.ok(employee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        List<EmployeeResponse> employees = service.getAll();
        if (Objects.isNull(employees) || employees.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(employees);
        }
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> saveNewEmployee(@RequestBody @Valid AddEmployeeRequest request) {
        EmployeeResponse response = service.saveNewEmployee(request);
        if (Objects.isNull(response)) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/{id}/fire")
    public ResponseEntity<EmployeeResponse> fireEmployee(@PathVariable @Digits(integer = 19, fraction = 0, message = "Employee ID must contain only digits!") @Valid Long id,
                                                         @RequestBody @Valid FireEmployeeRequest request) {
        EmployeeResponse response = service.fireEmployee(id, request);
        if (Objects.isNull(response)) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable @Digits(integer = 19, fraction = 0, message = "Employee ID must contain only digits!") @Valid Long id) {
        if (service.deleteEmployeeById(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public ResponseEntity<EmployeeResponse> editEmployeeById(@RequestHeader(name = "passportId") @Pattern(regexp = "[\\d]{8,}", message = "Passport ID must contain at least 8 digits!") @Valid String passportId,
                                                             @RequestBody @Valid EditEmployeeRequest request) {
        EmployeeResponse response = service.editEmployeeByPassportId(passportId, request);
        if (Objects.isNull(response)) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(response);
        }
    }
}
