package com.example.employee.controller;

import com.example.employee.models.Employee;
import com.example.employee.services.DepartmentService;
import com.example.employee.services.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class EmployeeController {
    Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    EmployeeService employeeService;
    @Autowired
    DepartmentService departmentService;

    //Get all the employees
    @GetMapping("/employees")
    public ResponseEntity<Object> getEmployees() throws Exception{
        List<Employee> employees = employeeService.getEmployees();

        LOGGER.info("GET request for all employees is successful");
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    //Get a specific employee
    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<Object> getEmployee(@PathVariable String employeeId) throws Exception{
        Long id = Long.parseLong(employeeId);
        Employee employee = employeeService.getEmployee(id);

        LOGGER.info("GET request is successful for employee with Id : "+employeeId);
        return new ResponseEntity<Object>(employee, HttpStatus.OK);
    }

    //Adding a new Employee
    @PostMapping("/departments/{deptId}/employees")
    public ResponseEntity<Object> createEmployee(@PathVariable String deptId, @Valid @RequestBody Employee employee) throws Exception {
        Long id = Long.parseLong(deptId);
        employeeService.createEmployee(id,employee);

        LOGGER.info("POST Request for employee is successful");
        return new ResponseEntity<>("Employee details added successfully", HttpStatus.CREATED);
    }

    //Updating the details of an employee
    @PutMapping("/departments/{deptId}/employees/{employeeId}")
    public ResponseEntity<Object> updateEmployee(@PathVariable String deptId, @PathVariable String employeeId, @Valid @RequestBody Employee employee) throws Exception{
        employeeService.updateEmployee(Long.parseLong(deptId), Long.parseLong(employeeId), employee);

        LOGGER.info("PUT Request is successful for employee with id : "+employeeId);
        return new ResponseEntity<>("Employee details have been successfully updated", HttpStatus.OK);
    }

    //Deleting the details of an employee
    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<Object> deleteEmployee(@PathVariable String employeeId) throws Exception{
        Employee employee = employeeService.deleteEmployee(Long.parseLong(employeeId));

        LOGGER.info("DELETE Request is successful for employee with id : "+employeeId);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }
}
