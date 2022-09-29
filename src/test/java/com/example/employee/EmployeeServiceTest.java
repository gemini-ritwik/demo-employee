package com.example.employee;

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
import com.example.employee.services.DepartmentServiceImpl;
import com.example.employee.services.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = EmployeeServiceTest.class)
public class EmployeeServiceTest {

    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    DepartmentRepository departmentRepository;

    @InjectMocks
    EmployeeServiceImpl employeeService;
    @InjectMocks
    DepartmentServiceImpl departmentService;

    private final ModelMapper modelMapper = new ModelMapper();

    @Test
    public void testGetEmployees() throws Exception {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1L,
                "Name 1",
                new Address("Address 1","City 1", "State 1", "pin1"),
                "Designation 1",
                "1234567890",
                true,
                false,
                null));
        employees.add(new Employee(2L,
                "Name 2",
                new Address("Address 2","City 2", "State 2", "pin2"),
                "Designation 2",
                "1234567890",
                true,
                false,
                null));

        when(employeeRepository.findByIsActiveAndIsDeleted(true, false))
                .thenReturn(employees);

        assertEquals(2, employeeService.getEmployees().size());
    }
    @Test
    public void testGetEmployeesThrowsNoDataFoundException() {
        List<Employee> employees = new ArrayList<>();

        when(employeeRepository.findByIsActiveAndIsDeleted(true, false))
                .thenReturn(employees);

        assertThatThrownBy(() -> employeeService.getEmployees())
                .isInstanceOf(NoDataFoundException.class);
    }

    @Test
    public void testGetEmployee() throws Exception {
        long employeeId = 1;
        Employee employee = new Employee(1L,
                "Name 2",
                new Address("Address 2","City 2", "State 2", "pin2"),
                "Designation 2",
                "1234567890",
                true,
                false,
                null);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        EmployeeDTO employeeDTO = employeeService.getEmployee(employeeId);
        assertEquals(employee.getEmployeeId(), employeeDTO.getEmployeeId());
        assertEquals(employee.getEmployeeName(), employeeDTO.getEmployeeName());
        assertEquals(employee.getEmployeeDesignation(), employeeDTO.getEmployeeDesignation());
        assertEquals(employee.getPhoneNumber(), employeeDTO.getPhoneNumber());
        assertEquals(employee.getEmployeeAddress().getAddress(), employeeDTO.getAddress());
        assertEquals(employee.getEmployeeAddress().getCity(), employeeDTO.getCity());
        assertEquals(employee.getEmployeeAddress().getState(), employeeDTO.getState());
        assertEquals(employee.getEmployeeAddress().getPincode(), employeeDTO.getPincode());
        assertEquals(employee.getDepartment(), employeeDTO.getDepartment());
    }
    @Test
    public void testGetEmployeeThrowsEmployeeNotFoundException() {
        long employeeId = 1;

        when(employeeRepository.findById(employeeId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getEmployee(employeeId))
                .isInstanceOf(EmployeeNotFoundException.class);
    }

    @Test
    public void testCreateEmployee() throws Exception {
        Employee employee = new Employee(2L,
                "Name 2",
                new Address("Address 2","City 2", "State 2", "pin2"),
                "Designation 2",
                "1234567890",
                false,
                false,
                null);
        long deptId = 1;
        Department department = new Department(deptId,
                "HR",
                "Description 1",
                1,
                1,
                true,
                false,
                null);

        when(departmentRepository.findById(deptId)).thenReturn(Optional.of(department));
        when(employeeRepository.save(employee)).thenReturn(employee);

        EmployeeDTO employeeDTO = employeeToEmployeeDTO(employee);

        employeeService.createEmployee(deptId, employeeDTO);

        verify(employeeRepository, times(1))
                .save(argThat(argument -> argument.getEmployeeName().equals(employee.getEmployeeName())
                        && argument.getEmployeeId().equals(employee.getEmployeeId())
                        && argument.getEmployeeDesignation().equals(employee.getEmployeeDesignation())
                        && argument.getPhoneNumber().equals(employee.getPhoneNumber())));
    }
    @Test
    public void testCreateEmployeeThrowsDepartmentNotFoundException() {
        Employee employee = new Employee(2L,
                "Name 2",
                new Address("Address 2","City 2", "State 2", "pin2"),
                "Designation 2",
                "1234567890",
                false,
                false,
                null);
        long deptId = 1;

        when(departmentRepository.findById(deptId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.createEmployee(deptId, employeeToEmployeeDTO(employee)))
                .isInstanceOf(DepartmentNotFoundException.class);
    }

    @Test
    public void testUpdateDepartment() throws Exception {
        long deptId = 1;
        Department department = new Department(deptId,
                "HR",
                "Description 1",
                1,
                1,
                true,
                false,
                null);
        long employeeId = 1;
        Employee oldEmployee = new Employee(employeeId,
                "Name 2",
                new Address("Address 2","City 2", "State 2", "pin2"),
                "Designation 2",
                "1234567890",
                true,
                false,
                department);
        Employee newEmployee = new Employee(employeeId,
                "Name 3",
                new Address("Address 3","City 3", "State 3", "pin3"),
                "Designation 5",
                "1234567890",
                false,
                false,
                null);

        when(departmentRepository.findById(deptId)).thenReturn(Optional.of(department));
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(oldEmployee));
        when(employeeRepository.save(oldEmployee)).thenReturn(oldEmployee);

        EmployeeDTO updatedEmployee = employeeService.updateEmployee(deptId,
                employeeId,
                employeeToEmployeeDTO(newEmployee));

        assertEquals(employeeId, updatedEmployee.getEmployeeId());
        assertEquals(newEmployee.getEmployeeName(), updatedEmployee.getEmployeeName());
        assertEquals(newEmployee.getEmployeeAddress().getAddress(), updatedEmployee.getAddress());
        assertEquals(newEmployee.getEmployeeAddress().getState(), updatedEmployee.getState());
        assertEquals(newEmployee.getEmployeeAddress().getCity(), updatedEmployee.getCity());
        assertEquals(newEmployee.getEmployeeAddress().getPincode(), updatedEmployee.getPincode());
        assertEquals(newEmployee.getEmployeeDesignation(), updatedEmployee.getEmployeeDesignation());
        assertEquals(newEmployee.getPhoneNumber(), updatedEmployee.getPhoneNumber());
        assertEquals(department.getDeptName(), updatedEmployee.getDepartment().getDeptName());
    }
    @Test
    public void testUpdateDepartmentThrowsEmployeeNotFoundException() {
        long deptId = 1;
        Department department = new Department(deptId,
                "HR",
                "Description 1",
                1,
                1,
                true,
                false,
                null);
        long employeeId = 1;
        Employee oldEmployee = new Employee(employeeId,
                "Name 2",
                new Address("Address 2","City 2", "State 2", "pin2"),
                "Designation 2",
                "1234567890",
                true,
                false,
                department);
        Employee newEmployee = new Employee(employeeId,
                "Name 3",
                new Address("Address 3","City 3", "State 3", "pin3"),
                "Designation 5",
                "1234567890",
                false,
                false,
                null);

        when(departmentRepository.findById(deptId))
                .thenReturn(Optional.of(department));
        when(employeeRepository.findById(employeeId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.updateEmployee(deptId,
                employeeId,
                employeeToEmployeeDTO(newEmployee)))
                .isInstanceOf(EmployeeNotFoundException.class);
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        long employeeId = 1;
        Employee employee = new Employee(1L,
                "Name 2",
                new Address("Address 2","City 2", "State 2", "pin2"),
                "Designation 2",
                "1234567890",
                true,
                false,
                null);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(employeeId);

        verify(employeeRepository, times(1)).save(employee);
        assertTrue(employee.isDeleted());
        assertFalse(employee.isActive());
    }
    @Test
    public void testDeleteEmployeeThrowsEmployeeNotFoundException() {
        long employeeId = 1;

        when(employeeRepository.findById(employeeId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.deleteEmployee(employeeId))
                .isInstanceOf(EmployeeNotFoundException.class);
    }



    //Department converter
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
    //Employee converter
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
