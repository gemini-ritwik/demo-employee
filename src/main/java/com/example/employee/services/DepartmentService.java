package com.example.employee.services;

import com.example.employee.models.Department;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DepartmentService {

    public List<Department> getDepartments() throws Exception;
    public Department getDepartment(Long deptId) throws Exception;
    public Department updateDepartment(Long deptId, Department department) throws Exception;
    public void createDepartment(Department department) throws Exception;
    public Department deleteDepartment(Long deptId) throws Exception;
}
