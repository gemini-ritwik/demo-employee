package com.example.employee.services;

import com.example.employee.dto.DepartmentDTO;
import com.example.employee.dto.EmployeeDTO;
import com.example.employee.exception.DepartmentNotFoundException;
import com.example.employee.exception.EmployeeNotFoundException;
import com.example.employee.exception.NoDataFoundException;
import com.example.employee.models.Address;
import com.example.employee.models.Department;
import com.example.employee.models.Employee;
import com.example.employee.repository.DepartmentRepository;
import com.example.employee.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    private final ModelMapper modelMapper=new ModelMapper();

    /**
     * Fetches all the employees from the database
     * @return List<EmployeeDTO> Returns a list of all the employees
     * @throws Exception Throws exception when there are no employees
     */
    @Override
    public List<EmployeeDTO> getEmployees() throws Exception{
        LOGGER.trace("Entering the method getEmployees.");

        List<EmployeeDTO> employees = employeeRepository
                .findByIsActiveAndIsDeleted(true,false)
                .stream()
                .map(this::employeeToEmployeeDTO)
                .collect(Collectors.toList());

        if(employees.isEmpty()) {
            LOGGER.error("No data found in the employee table");
            throw new NoDataFoundException("There is no data in the employee table");
        }

        LOGGER.info("Fetched all the employees from the database");
        return employees;
    }

    /**
     * Fetches the employee with the given employee id from the database
     * @param employeeId Employee id of the employee to be fetched
     * @return EmployeeDTO Returns the employee with the given employee id
     * @throws Exception Throws an exception when the employee with the given id does not exist
     */
    @Override
    public EmployeeDTO getEmployee(Long employeeId) throws Exception{
        LOGGER.trace("Entering the method getEmployee");

        Employee employeeFromDb = employeeRepository.findById(employeeId).orElseThrow(
                () -> {
                    LOGGER.error("Employee not found with id : "+employeeId);
                    return new EmployeeNotFoundException("Employee not found with id : "+employeeId);
                }
        );

        if(!employeeFromDb.isActive() && employeeFromDb.isDeleted()) {
            LOGGER.error("Employee not found with id : "+employeeId);
            throw new EmployeeNotFoundException("Employee not found with id : " + employeeId);
        }

        LOGGER.info("Fetched employee with id : "+employeeId);
        return employeeToEmployeeDTO(employeeFromDb);
    }

    /**
     * Update the details of the employee with given id
     * @param deptId id of the department to which the employee belongs
     * @param employeeId Employee id of the employee to be updated
     * @param employeeDTO employee details of to be updated
     * @return EmployeeDTO Returns the updated employee
     * @throws Exception Throws an exception when the employee or department with given ids don't exist
     */
    @Override
    public EmployeeDTO updateEmployee(Long deptId, Long employeeId, EmployeeDTO employeeDTO) throws Exception{
        LOGGER.trace("Entering the method updateEmployees");

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

        if(!employeeFromDb.isActive() && employeeFromDb.isDeleted()) {
            LOGGER.error("Employee not found with id : "+employeeId);
            throw new EmployeeNotFoundException("Employee not found with id : " + employeeId);
        }
        if(!departmentFromDb.isActive() && departmentFromDb.isDeleted()) {
            LOGGER.error("Department not found with id : "+deptId);
            throw new DepartmentNotFoundException("Department not found with deptId : " + deptId);
        }

        Employee employee = employeeDTOToEmployee(employeeDTO);
        employee.getEmployeeAddress().setAddressId(employeeFromDb.getEmployeeAddress().getAddressId());

        LOGGER.debug("Updating the employee with id : "+employeeId+" from : "+employeeFromDb+" to : "+employee);

        employeeFromDb.setEmployeeName(employee.getEmployeeName());
        employeeFromDb.setEmployeeAddress(employee.getEmployeeAddress());
        employeeFromDb.setEmployeeDesignation(employee.getEmployeeDesignation());
        employeeFromDb.setPhoneNumber(employee.getPhoneNumber());
        employeeFromDb.getEmployeeAddress().setActive(true);
        employeeFromDb.setDepartment(departmentFromDb);

        employeeRepository.save(employeeFromDb);
        LOGGER.info("Employee details updated with id : "+employeeId);

        return employeeToEmployeeDTO(employeeFromDb);
    }

    /**
     * Save the employee to the database belonging to the specific department
     * @param deptId id of the department to which the employee belongs
     * @param employeeDTO employee to be saved
     * @throws Exception thr/ows an exception when the department with the given id does not exist
     */
    @Override
    public void createEmployee(Long deptId, EmployeeDTO employeeDTO) throws Exception{
        LOGGER.trace("Entering the method createEmployee");

        Employee employee = employeeDTOToEmployee(employeeDTO);
        employee.setActive(true);
        employee.setDeleted(false);
        employee.getEmployeeAddress().setActive(true);
        Department department = departmentRepository.
                findById(deptId)
                .orElseThrow(
                () -> {
                    LOGGER.error("Department not found to add employee to, with id : "+deptId);
                    return new DepartmentNotFoundException("Department not found with deptId : "+deptId);
                });
        if(!department.isActive() && department.isDeleted()) {
            LOGGER.error("Department not found to add employee to, with id : "+deptId);
            throw new DepartmentNotFoundException("Department not found with deptId : "+deptId);
        }
        employee.setDepartment(department);
        employeeRepository.save(employee);
        LOGGER.info("Employee created successfully");
    }

    /**
     * Deletes the employee with the given employee id
     * @param employeeId id of the employee to be deleted
     * @return Employee returns the employee that has been deleted
     * @throws Exception throws exception when the employee to be deleted does not exist in the database
     */
    @Override
    public EmployeeDTO deleteEmployee(Long employeeId) throws Exception{
        LOGGER.trace("Entering the method deleteEmployee");

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
        return employeeToEmployeeDTO(employee);
    }



    public Department departmentDTOToDepartment(DepartmentDTO departmentDTO)
    {
        Department department = this.modelMapper.map(departmentDTO, Department.class);
        department.setActive(true);
        return department;
    }
    public DepartmentDTO departmentToDepartmentDTO(Department department)
    {
        DepartmentDTO departmentDTO = this.modelMapper.map(department, DepartmentDTO.class);
        return departmentDTO;
    }
    public Employee employeeDTOToEmployee(EmployeeDTO employeeDTO)
    {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        Employee employee = this.modelMapper.map(employeeDTO, Employee.class);
        Address address = new Address(employee.getEmployeeAddress().getAddress(),
                employee.getEmployeeAddress().getCity(),
                employee.getEmployeeAddress().getState(),
                employee.getEmployeeAddress().getPincode());
        employee.setActive(true);
        employee.setEmployeeAddress(address);
        return employee;
    }
    public EmployeeDTO employeeToEmployeeDTO(Employee employee)
    {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        EmployeeDTO employeeDTO = this.modelMapper.map(employee, EmployeeDTO.class);
        return employeeDTO;
    }
}
