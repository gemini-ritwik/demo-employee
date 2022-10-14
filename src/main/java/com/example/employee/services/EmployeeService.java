package com.example.employee.services;

import com.example.employee.dto.EmployeeDTO;

import java.util.List;

public interface EmployeeService {

    /**
     * Fetches all the employees from the database
     * @return List<EmployeeDTO> Returns a list of all the employees
     * @throws Exception Throws exception when there are no employees
     */
    public List<EmployeeDTO> getEmployees() throws Exception;

    /**
     * Fetches the employee with the given employee id from the database
     * @param employeeId Employee id of the employee to be fetched
     * @return EmployeeDTO Returns the employee with the given employee id
     * @throws Exception Throws an exception when the employee with the given id does not exist
     */
    public EmployeeDTO getEmployee(Long employeeId) throws Exception;

    /**
     * Update the details of the employee with given id
     * @param deptId id of the department to which the employee belongs
     * @param employeeId Employee id of the employee to be updated
     * @param employeeDTO employee details of to be updated
     * @return EmployeeDTO Returns the updated employee
     * @throws Exception Throws an exception when the employee or department with given ids don't exist
     */
    public EmployeeDTO updateEmployee(Long deptId, Long employeeId, EmployeeDTO employeeDTO) throws Exception;

    /**
     * Save the employee to the database belonging to the specific department
     * @param deptId id of the department to which the employee belongs
     * @param employeeDTO employee to be saved
     * @throws Exception thr/ows an exception when the department with the given id does not exist
     */
    public void createEmployee(Long deptId, EmployeeDTO employeeDTO) throws Exception;

    /**
     * Deletes the employee with the given employee id
     * @param employeeId id of the employee to be deleted
     * @return Employee returns the employee that has been deleted
     * @throws Exception throws exception when the employee to be deleted does not exist in the database
     */
    public EmployeeDTO deleteEmployee(Long employeeId) throws Exception;
}
