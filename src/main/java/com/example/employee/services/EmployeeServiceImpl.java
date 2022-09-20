package com.example.employee.services;

import com.example.employee.exception.DepartmentNotFoundException;
import com.example.employee.exception.EmployeeNotFoundException;
import com.example.employee.exception.NoDataFoundException;
import com.example.employee.models.Department;
import com.example.employee.models.Employee;
import com.example.employee.repository.DepartmentRepository;
import com.example.employee.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public List<Employee> getEmployees() throws Exception{
        LOGGER.trace("Entering the method getEmployees.");

        List<Employee> employees = new ArrayList<>();

        employeeRepository.findAll().forEach(employee -> {
            //Check if record is active
            if(employee.isActive())
                employees.add(employee);
        });

        //Check if the list of data is empty
        if(employees.isEmpty()) {
            LOGGER.error("No data found in the employee table");
            throw new NoDataFoundException("There is no data in the employee table");
        }

        LOGGER.info("Fetched all the employees from the database");
        return employees;
    }

    @Override
    public Employee getEmployee(Long employeeId) throws Exception{
        LOGGER.trace("Entering the method getEmployee");

        //getting record from the DB
        Employee employeeFromDb = employeeRepository.findById(employeeId).orElseThrow(
                () -> {
                    LOGGER.error("Employee not found with id : "+employeeId);
                    return new EmployeeNotFoundException("Employee not found with id : "+employeeId);
                }
        );

        //Check if the record is active and not deleted
        if(!employeeFromDb.isActive()) {
            LOGGER.error("Employee not found with id : "+employeeId);
            throw new EmployeeNotFoundException("Employee not found with id : " + employeeId);
        }

        LOGGER.info("Fetched employee with id : "+employeeId);
        return employeeFromDb;
    }

    @Override
    public Employee updateEmployee(Long deptId, Long employeeId, Employee employee) throws Exception{
        LOGGER.trace("Entering the method updateEmployees");

        //getting record from the DB
        Employee employeeFromDb = employeeRepository.findById(employeeId).orElseThrow(
                () -> {
                    LOGGER.error("Employee not found with id : "+employeeId);
                    return new EmployeeNotFoundException("Employee not found with id : "+employeeId);
                }
        );
        Department departmentFromDb = departmentRepository.findById(deptId).orElseThrow(
                () -> {
                    LOGGER.error("Department not found with id : "+deptId);
                    return new DepartmentNotFoundException("Department not found with deptId : "+deptId);
                }
        );

        //Check if the record is active and not deleted
        if(!employeeFromDb.isActive()) {
            LOGGER.error("Employee not found with id : "+employeeId);
            throw new EmployeeNotFoundException("Employee not found with id : " + employeeId);
        }
        if(!departmentFromDb.isActive()) {
            LOGGER.error("Department not found with id : "+deptId);
            throw new DepartmentNotFoundException("Department not found with deptId : " + deptId);
        }

        LOGGER.debug("Updating the employee with id : "+employeeId+" from : "+employeeFromDb+" to : "+employee);
        //Updating data
        employeeFromDb.setEmployeeName(employee.getEmployeeName());
        employeeFromDb.setEmployeeAddress(employee.getEmployeeAddress());
        employeeFromDb.setEmployeeDesignation(employee.getEmployeeDesignation());
        employeeFromDb.setPhoneNumber(employee.getPhoneNumber());
        employeeFromDb.getEmployeeAddress().setActive(true);
        employeeFromDb.setDepartment(departmentFromDb);

        //Saving to the db
        employeeRepository.save(employeeFromDb);
        LOGGER.info("Employee details updated with id : "+employeeId);

        return employeeFromDb;
    }

    @Override
    public void createEmployee(Long deptId, Employee employee) throws Exception{
        LOGGER.trace("Entering the method createEmployee");

        employee.setActive(true);
        employee.setDeleted(false);
        employee.getEmployeeAddress().setActive(true);
        Optional<Department> department = departmentRepository.findById(deptId);
        employee.setDepartment(department.orElseThrow(
                () -> {
                    LOGGER.error("Department not found to add employee to, with id : "+deptId);
                    return new DepartmentNotFoundException("Department not found with deptId : "+deptId);
                }
        ));
        employeeRepository.save(employee);
        LOGGER.info("Employee created successfully");
    }

    @Override
    public Employee deleteEmployee(Long employeeId) throws Exception{
        LOGGER.trace("Entering the method deleteEmployee");

        //Getting employee from db
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> {
                    LOGGER.error("Employee not found with id : "+employeeId);
                    return new EmployeeNotFoundException("Employee not found with id : "+employeeId);
                }
        );

        employee.setDeleted(true);
        employee.setActive(false);
        employee.getEmployeeAddress().setActive(false);
        employee.getEmployeeAddress().setDeleted(true);
        employeeRepository.save(employee);

        LOGGER.info("Employee deleted with id : "+employeeId);
        return employee;
    }

}
