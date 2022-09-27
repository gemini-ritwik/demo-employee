package com.example.employee;

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
        employees.add(new Employee(1L,
                "Name 3",
                new Address("Address 3","City 3", "State 3", "pin"),
                "Designation 3",
                "1234567890",
                false,
                true,
                null));

        when(employeeRepository.findAll()).thenReturn(employees);

        assertEquals(2, employeeService.getEmployees().size());
    }
    @Test
    public void testGetEmployeesThrowsNoDataFoundException() {
        List<Employee> employees = new ArrayList<>();

        when(employeeRepository.findAll())
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

        assertEquals(employee, employeeService.getEmployee(employeeId));
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

        assertFalse(employee.isActive());

        employeeService.createEmployee(deptId, employee);

        assertTrue(employee.isActive());
        assertEquals(department, employee.getDepartment());
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

        assertThatThrownBy(() -> employeeService.createEmployee(deptId, employee))
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

        Employee updatedEmployee = employeeService.updateEmployee(deptId, employeeId, newEmployee);

        assertEquals(employeeId, updatedEmployee.getEmployeeId());
        assertEquals(newEmployee.getEmployeeName(), updatedEmployee.getEmployeeName());
        assertEquals(newEmployee.getEmployeeAddress(), updatedEmployee.getEmployeeAddress());
        assertEquals(newEmployee.getEmployeeDesignation(), updatedEmployee.getEmployeeDesignation());
        assertEquals(newEmployee.getPhoneNumber(), updatedEmployee.getPhoneNumber());
        assertEquals(department, updatedEmployee.getDepartment());
        verify(employeeRepository, times(1)).save(updatedEmployee);
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

        assertThatThrownBy(() -> employeeService.updateEmployee(deptId, employeeId, newEmployee))
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
}
