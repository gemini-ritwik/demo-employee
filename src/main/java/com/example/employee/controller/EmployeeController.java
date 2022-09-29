package com.example.employee.controller;

import com.example.employee.dto.DepartmentDTO;
import com.example.employee.dto.EmployeeDTO;
import com.example.employee.services.DepartmentService;
import com.example.employee.services.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Get all Employees", description = "Get all the employees", tags = "Employees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DepartmentDTO.class))}),
            @ApiResponse(responseCode = "404", description = "No Employee Found",
                    content = @Content)
    })
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
    @Operation(summary = "Get employee by Id", description = "Fetch Employee using employee Id", tags = "Employees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Employee",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DepartmentDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content)
    })
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
    @Operation(summary = "Add Employee", description = "Add an employee", tags = "Employees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee Added",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content)
    })
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
    @Operation(summary = "Update Employee", description = "Update the Employee", tags = "Employees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee Updated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Employee to be updated not found",
                    content = @Content)
    })
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
    @Operation(summary = "Delete Employee", description = "Delete the employee", tags = "Employees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee Deleted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DepartmentDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Employee to be deleted not found",
                    content = @Content)
    })
    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<Object> deleteEmployee(@PathVariable String employeeId) throws Exception{
        EmployeeDTO employee = employeeService.deleteEmployee(Long.parseLong(employeeId));

        LOGGER.info("DELETE Request is successful for employee with id : "+employeeId);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }
}
