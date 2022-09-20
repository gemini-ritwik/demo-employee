package com.example.employee.services;

import com.example.employee.models.Employee;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmployeeService {

    public List<Employee> getEmployees() throws Exception;
    public Employee getEmployee(Long employeeId) throws Exception;
    public Employee updateEmployee(Long deptId, Long employeeId, Employee employee) throws Exception;
    public void createEmployee(Long deptId, Employee employee) throws Exception;
    public Employee deleteEmployee(Long employeeId) throws Exception;
}
