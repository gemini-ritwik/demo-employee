package com.example.employee.services;

import com.example.employee.dto.EmployeeDTO;

import java.util.List;

public interface EmployeeService {

    public List<EmployeeDTO> getEmployees() throws Exception;
    public EmployeeDTO getEmployee(Long employeeId) throws Exception;
    public EmployeeDTO updateEmployee(Long deptId, Long employeeId, EmployeeDTO employeeDTO) throws Exception;
    public void createEmployee(Long deptId, EmployeeDTO employeeDTO) throws Exception;
    public EmployeeDTO deleteEmployee(Long employeeId) throws Exception;
}
