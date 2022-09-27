package com.example.employee.controller;

import com.example.employee.models.Department;
import com.example.employee.services.DepartmentService;
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

    //Get all the departments
    @GetMapping("/departments")
    public ResponseEntity<Object> getDepartments() throws Exception {
        List<Department> departments = departmentService.getDepartments();

        LOGGER.info("GET Request for all departments is successful");
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }

    //Get a specific department
    @GetMapping("/departments/{deptId}")
    public ResponseEntity<Object> getDepartment(@PathVariable String deptId) throws Exception {
        Long id = Long.parseLong(deptId);
        Department department = departmentService.getDepartment(id);

        LOGGER.info("GET Request is successful for department with id : "+deptId);
        return new ResponseEntity<Object>(department, HttpStatus.OK);
    }

    //Adding a new Department
    @PostMapping("/departments")
    public ResponseEntity<Object> createDepartment(@Valid @RequestBody Department department) throws Exception{
        departmentService.createDepartment(department);

        LOGGER.info("POST Request for department is successful");
        return new ResponseEntity<>("Department details added successfully", HttpStatus.CREATED);
    }

    //Updating the details of a department
    @PutMapping("/departments/{deptId}")
    public ResponseEntity<Object> updateDepartment(@PathVariable String deptId, @Valid @RequestBody Department department) throws Exception{
        Long id = Long.parseLong(deptId);
        departmentService.updateDepartment(id, department);

        LOGGER.info("PUT Request is successful for department with id : "+deptId);
        return new ResponseEntity<>("Department details have been successfully updated", HttpStatus.OK);
    }

    //Deleting the details of a department
    @DeleteMapping("/departments/{deptId}")
    public ResponseEntity<Object> deleteDepartment(@PathVariable String deptId) throws Exception{
        Department dept = departmentService.deleteDepartment(Long.parseLong(deptId));

        LOGGER.info("DELETE Request is successful for department with id : "+deptId);
        return new ResponseEntity<>(dept, HttpStatus.OK);
    }
}
