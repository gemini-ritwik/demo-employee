package com.example.employee.services;

import com.example.employee.dto.DepartmentDTO;

import java.util.List;

public interface DepartmentService {

    /**
     * Fetches all the departments from the database.
     * @return List<Department> This returns all the departments
     * @throws Exception Throws exception when there are no departments in the database
     */
    public List<DepartmentDTO> getDepartments() throws Exception;

    /**
     * Fetches the department with a specific department id
     * @param deptId Department id of the department to be fetched
     * @return Department Returns the department with deptId
     * @throws Exception Throws exception when the department with given id does not exist
     */
    public DepartmentDTO getDepartment(Long deptId) throws Exception;

    /**
     * Updates the details of department with the given department id
     * @param deptId Department id of the department to be updated
     * @param departmentDTO Department details with which existing department is to be replaced
     * @return  Department Returns the updated department
     * @throws Exception Throws exception when the department to be updated does not exist
     */
    public DepartmentDTO updateDepartment(Long deptId, DepartmentDTO departmentDTO) throws Exception;

    /**
     * Saves a department to the database
     * @param departmentDTO Department to be saved in database
     */
    public void createDepartment(DepartmentDTO departmentDTO) throws Exception;

    /**
     * Delete the department with the given department id
     * @param deptId Department id of the department to be deleted
     * @return Department Returns the department that has been deleted
     * @throws Exception Throws exception when the department to be deleted does not exist
     */
    public DepartmentDTO deleteDepartment(Long deptId) throws Exception;
}
