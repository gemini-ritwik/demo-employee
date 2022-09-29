package com.example.employee.controller;

import com.example.employee.dto.DepartmentDTO;
import com.example.employee.services.DepartmentService;
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
public class DepartmentController {
    Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    DepartmentService departmentService;

    /**
     * API to fetch all the departments
     *
     * @return List<Departments>
     * @throws Exception Throws exception when list is empty
     */
    @Operation(summary = "Get all departments", description = "Get all the departments", tags = "Departments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Departments",
                    content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = DepartmentDTO.class))}),
            @ApiResponse(responseCode = "404", description = "No Department Found",
                    content = @Content)
    })
    @GetMapping("/departments")
    public ResponseEntity<Object> getDepartments() throws Exception {
        List<DepartmentDTO> departments = departmentService.getDepartments();

        LOGGER.info("GET Request for all departments is successful");
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }

    /**
     * API to fetch department by department id
     *
     * @param deptId Department id of the department to be fetched
     * @return Department
     * @throws Exception Throws exception when department does not exist
     */
    @Operation(summary = "Get department by Id", description = "Fetch Department using department Id", tags = "Departments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the department",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DepartmentDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Department not found",
                    content = @Content)
    })
    @GetMapping("/departments/{deptId}")
    public ResponseEntity<Object> getDepartment(@PathVariable String deptId) throws Exception {
        Long id = Long.parseLong(deptId);
        DepartmentDTO department = departmentService.getDepartment(id);

        LOGGER.info("GET Request is successful for department with id : "+deptId);
        return new ResponseEntity<Object>(department, HttpStatus.OK);
    }

    /**
     * API to create a new department
     *
     * @param department Department to be created
     * @return Success Message
     * @throws Exception when bad request
     */
    @Operation(summary = "Add Department", description = "Add a departments", tags = "Departments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Department Added",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content)
    })
    @PostMapping("/departments")
    public ResponseEntity<Object> createDepartment(@Valid @RequestBody DepartmentDTO department) throws Exception{
        departmentService.createDepartment(department);

        LOGGER.info("POST Request for department is successful");
        return new ResponseEntity<>("Department details added successfully", HttpStatus.CREATED);
    }

    /**
     * API to update the department
     *
     * @param deptId Department id of the employee to be updated
     * @param department Department details with which department is to be updated
     * @return Success Message
     * @throws Exception when bad request
     */
    @Operation(summary = "Update Department", description = "Update the Department", tags = "Departments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Department Updated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Department to be updated not found",
                    content = @Content)
    })
    @PutMapping("/departments/{deptId}")
    public ResponseEntity<Object> updateDepartment(@PathVariable String deptId, @Valid @RequestBody DepartmentDTO department) throws Exception{
        Long id = Long.parseLong(deptId);
        departmentService.updateDepartment(id, department);

        LOGGER.info("PUT Request is successful for department with id : "+deptId);
        return new ResponseEntity<>("Department details have been successfully updated", HttpStatus.OK);
    }

    /**
     * API to delete the department by department id
     *
     * @param deptId Department id of the department to be deleted
     * @return Department
     * @throws Exception when department to be deleted does not exist
     */
    @Operation(summary = "Delete Department", description = "Delete the departments", tags = "Departments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Department Deleted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DepartmentDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Department to be deleted not found",
                    content = @Content)
    })
    @DeleteMapping("/departments/{deptId}")
    public ResponseEntity<Object> deleteDepartment(@PathVariable String deptId) throws Exception{
        DepartmentDTO dept = departmentService.deleteDepartment(Long.parseLong(deptId));

        LOGGER.info("DELETE Request is successful for department with id : "+deptId);
        return new ResponseEntity<>(dept, HttpStatus.OK);
    }
}
