package com.example.employee;

import com.example.employee.controller.EmployeeController;
import com.example.employee.exception.EmployeeNotFoundException;
import com.example.employee.exception.GlobalExceptionHandler;
import com.example.employee.exception.NoDataFoundException;
import com.example.employee.models.Address;
import com.example.employee.models.Department;
import com.example.employee.models.Employee;
import com.example.employee.services.EmployeeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ComponentScan(basePackages = "com.example.employee")
@AutoConfigureMockMvc
@ContextConfiguration
@SpringBootTest(classes = EmployeeServiceTest.class)
public class EmployeeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    EmployeeServiceImpl employeeService;

    @InjectMocks
    EmployeeController employeeController;

    List<Employee> employees;
    Employee employee;
    Department department;

    @BeforeEach
    public void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(employeeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void testGetEmployees() throws Exception {
        employees = new ArrayList<>();
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

        when(employeeService.getEmployees()).thenReturn(employees);

        this.mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    public void testGetEmployeesNotFound() throws Exception {
        when(employeeService.getEmployees())
                .thenThrow(new NoDataFoundException("There is no data in the employee table"));


        this.mockMvc.perform(get("/employees"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath(".message")
                        .value("There is no data in the employee table"))
                .andDo(print());
    }

    @Test
    public void testGetEmployee() throws Exception{
        long employeeId = 1;
        Employee employee = new Employee(1L,
                "Name 1",
                new Address("Address 1","City 1", "State 1", "pin1"),
                "Designation 1",
                "1234567890",
                true,
                false,
                null);

        when(employeeService.getEmployee(employeeId)).thenReturn(employee);

        this.mockMvc.perform(get("/employees/{employeeId}", employeeId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(".employeeId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath(".employeeName").value("Name 1"))
                .andExpect(MockMvcResultMatchers.jsonPath(".employeeDesignation").value("Designation 1"))
                .andExpect(MockMvcResultMatchers.jsonPath(".phoneNumber").value("1234567890"))
                .andDo(print());
    }
    @Test
    public void testGetEmployeeNotFound() throws Exception {
        long employeeId = 1;
        when(employeeService.getEmployee(employeeId))
                .thenThrow(new EmployeeNotFoundException("Employee not found with id : "+employeeId));


        this.mockMvc.perform(get("/employees/{employeeId}", employeeId))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath(".message")
                        .value("Employee not found with id : 1"))
                .andDo(print());
    }

    @Test
    public void testCreateEmployee() throws Exception {
        long deptId = 1;
        department = new Department(deptId, "HR", "Description 1", 1, 1, true, false, null);
        long employeeId = 1;
        Employee employee = new Employee(1L,
                "Name 1",
                new Address("Address 1","City 1", "State 1", "pin1"),
                "Designation 1",
                "1234567890",
                true,
                false,
                department);

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(employee);

        this.mockMvc.perform(post("/departments/{deptId}/employees", deptId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }
    @Test
    public void testCreateEmployeeBadRequest() throws Exception {
        long deptId = 1;
        department = new Department(deptId, "HR",
                "Description 1",
                1,
                1,
                true,
                false,
                null);
        long employeeId = 1;
        Employee employee = new Employee(1L,
                "",
                new Address("Address 1","City 1", "State 1", "pin1"),
                "Designation 1",
                "",
                true,
                false,
                department);

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(employee);

        this.mockMvc.perform(post("/departments/{deptId}/employees", deptId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath(".fieldErrors.employeeName")
                        .value("Employee name should not be empty."))
                .andExpect(MockMvcResultMatchers.jsonPath(".fieldErrors.phoneNumber")
                        .value("Number should contain 10 digits."))
                .andDo(print());
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        long deptId = 1;
        department = new Department(deptId, "HR", "Description 1", 1, 1, true, false, null);
        long employeeId = 1;
        Employee employee = new Employee(1L,
                "Name 1",
                new Address("Address 1","City 1", "State 1", "pin1"),
                "Designation 1",
                "1234567890",
                true,
                false,
                department);

        when(employeeService.updateEmployee(deptId, employeeId, employee)).thenReturn(employee);

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(employee);

        this.mockMvc.perform(put("/departments/{deptId}/employees/{employeeId}", deptId, employeeId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    public void testUpdateEmployeeBadRequest() throws Exception {
        long deptId = 1;
        department = new Department(deptId, "HR", "Description 1", 1, 1, true, false, null);
        long employeeId = 1;
        Employee employee = new Employee(1L,
                "",
                new Address("Address 1","City 1", "State 1", "pin1"),
                "Designation 1",
                "hello",
                true,
                false,
                department);


        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(employee);

        this.mockMvc.perform(put("/departments/{deptId}/employees/{employeeId}", deptId, employeeId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath(".fieldErrors.employeeName")
                        .value("Employee name should not be empty."))
                .andExpect(MockMvcResultMatchers.jsonPath(".fieldErrors.phoneNumber")
                        .value("Number should contain 10 digits."))
                .andDo(print());
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        long deptId = 1;
        department = new Department(deptId, "HR", "Description 1", 1, 1, true, false, null);
        long employeeId = 1;
        Employee employee = new Employee(1L,
                "Name 1",
                new Address("Address 1","City 1", "State 1", "pin1"),
                "Designation 1",
                "1234567890",
                true,
                false,
                department);

        when(employeeService.deleteEmployee(employeeId)).thenReturn(employee);

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(employee);

        this.mockMvc.perform(delete("/employees/{employeeId}", employeeId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(".employeeId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath(".employeeName").value("Name 1"))
                .andExpect(MockMvcResultMatchers.jsonPath(".employeeDesignation").value("Designation 1"))
                .andExpect(MockMvcResultMatchers.jsonPath(".phoneNumber").value("1234567890"))
                .andDo(print());
    }
    @Test
    public void testDeleteEmployeeNotFound() throws Exception {
        long employeeId = 1;

        when(employeeService.deleteEmployee(employeeId))
                .thenThrow(new EmployeeNotFoundException("Employee not found with id : "+employeeId));

        this.mockMvc.perform(delete("/employees/{employeeId}", employeeId))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath(".message")
                        .value("Employee not found with id : 1"))
                .andDo(print());
    }
}
