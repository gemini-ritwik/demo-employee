package com.example.employee.services;

import com.example.employee.dto.DepartmentDTO;
import com.example.employee.models.Department;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DepartmentService {

    public List<DepartmentDTO> getDepartments() throws Exception;
    public DepartmentDTO getDepartment(Long deptId) throws Exception;
    public DepartmentDTO updateDepartment(Long deptId, DepartmentDTO departmentDTO) throws Exception;
    public void createDepartment(DepartmentDTO departmentDTO) throws Exception;
    public DepartmentDTO deleteDepartment(Long deptId) throws Exception;
}
