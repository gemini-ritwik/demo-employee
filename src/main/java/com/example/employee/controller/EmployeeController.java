package com.example.employee.controller;

import com.example.employee.dto.EmployeeDTO;
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

    /**
     * API to fetch all the employees
     *
     * @return List<Employee>
     * @throws Exception when there are no employees
     */
    @GetMapping("/employees")
    public ResponseEntity<Object> getEmployees() throws Exception{
        List<EmployeeDTO> employees = employeeService.getEmployees();

        LOGGER.info("GET request for all employees is successful");
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    /**
     * API to fetch employee by employee id
     *
     * @param employeeId Employee id of the employee to be fetched
     * @return Employee
     * @throws Exception when employee does not exist
     */
    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<Object> getEmployee(@PathVariable String employeeId) throws Exception{
        Long id = Long.parseLong(employeeId);
        EmployeeDTO employee = employeeService.getEmployee(id);

        LOGGER.info("GET request is successful for employee with Id : "+employeeId);
        return new ResponseEntity<Object>(employee, HttpStatus.OK);
    }

    /**
     * API to create a new employee
     *
     * @param deptId Department id of the department to which the employee belongs to
     * @param employeeDTO Employee details that are to be created
     * @return Success Message
     * @throws Exception when bad request
     */
    @PostMapping("/departments/{deptId}/employees")
    public ResponseEntity<Object> createEmployee(@PathVariable String deptId, @Valid @RequestBody EmployeeDTO employeeDTO) throws Exception {
        Long id = Long.parseLong(deptId);
        employeeService.createEmployee(id,employeeDTO);

        LOGGER.info("POST Request for employee is successful");
        return new ResponseEntity<>("Employee details added successfully", HttpStatus.CREATED);
    }

    /**
     * API to update the details of the employee
     *
     * @param deptId Department id of the department to which the employee exist
     * @param employeeId Employee id of the employee to be updated
     * @param employeeDTO Employee details with which employee is to be updated
     * @return Success Message
     * @throws Exception when bad request
     */
    @PutMapping("/departments/{deptId}/employees/{employeeId}")
    public ResponseEntity<Object> updateEmployee(@PathVariable String deptId, @PathVariable String employeeId, @Valid @RequestBody EmployeeDTO employeeDTO) throws Exception{
        employeeService.updateEmployee(Long.parseLong(deptId), Long.parseLong(employeeId), employeeDTO);

        LOGGER.info("PUT Request is successful for employee with id : "+employeeId);
        return new ResponseEntity<>("Employee details have been successfully updated", HttpStatus.OK);
    }

    /**
     * API to delete the employee
     *
     * @param employeeId Employee id of the employee to be deleted
     * @return Employee
     * @throws Exception when employee to be deleted does not exist
     */
    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<Object> deleteEmployee(@PathVariable String employeeId) throws Exception{
        EmployeeDTO employee = employeeService.deleteEmployee(Long.parseLong(employeeId));

        LOGGER.info("DELETE Request is successful for employee with id : "+employeeId);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }
}
