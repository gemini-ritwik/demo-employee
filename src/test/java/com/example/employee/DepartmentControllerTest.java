package com.example.employee;

import com.example.employee.controller.DepartmentController;
import com.example.employee.dto.DepartmentDTO;
import com.example.employee.exception.DepartmentNotFoundException;
import com.example.employee.exception.GlobalExceptionHandler;
import com.example.employee.exception.NoDataFoundException;
import com.example.employee.services.DepartmentServiceImpl;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ComponentScan(basePackages = "com.example.employee")
@AutoConfigureMockMvc
@ContextConfiguration
@SpringBootTest(classes = DepartmentServiceTest.class)
public class DepartmentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    DepartmentServiceImpl departmentService;

    @InjectMocks
    DepartmentController departmentController;

    List<DepartmentDTO> departments;
    DepartmentDTO department;
    Date date = new Date();

    @BeforeEach
    public void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(departmentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void testGetDepartments() throws Exception {
        departments = new ArrayList<>();
        departments.add(new DepartmentDTO(1L,
                "HR",
                "Description 1",
                1,
                1,
                new Timestamp(date.getTime()),
                new Timestamp(date.getTime())));
        departments.add(new DepartmentDTO(2L,
                "DevOps",
                "Description 2",
                2,
                2,
                new Timestamp(date.getTime()),
                new Timestamp(date.getTime())));
        departments.add(new DepartmentDTO(3L,
                "Design",
                "Description 3",
                1,
                1,
                new Timestamp(date.getTime()),
                new Timestamp(date.getTime())));

        when(departmentService.getDepartments()).thenReturn(departments);

        this.mockMvc.perform(get("/departments"))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    public void testGetDepartmentsStatusNotFound() throws Exception{
        when(departmentService.getDepartments())
                .thenThrow(new NoDataFoundException("There is no data in the department table"));


        this.mockMvc.perform(get("/departments"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath(".message")
                        .value("There is no data in the department table"))
                .andDo(print());
    }

    @Test
    public void testGetDepartment() throws Exception {
        long deptId = 1;
        department = new DepartmentDTO(deptId,
                "HR",
                "Description 1",
                1,
                1,
                new Timestamp(date.getTime()),
                new Timestamp(date.getTime()));

        when(departmentService.getDepartment(deptId)).thenReturn(department);

        this.mockMvc.perform(get("/departments/{deptId}", deptId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(".deptId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath(".deptName").value("HR"))
                .andExpect(MockMvcResultMatchers.jsonPath(".deptDescription").value("Description 1"))
                .andExpect(MockMvcResultMatchers.jsonPath(".createdBy").value(1))
                .andDo(print());
    }
    @Test
    public void testGetDepartmentStatusNotFound() throws Exception {
        long deptId = 1;

        when(departmentService.getDepartment(deptId))
                .thenThrow(new DepartmentNotFoundException("Department not found with id : "+deptId));


        this.mockMvc.perform(get("/departments/{deptId}", deptId))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath(".message")
                        .value("Department not found with id : 1"))
                .andDo(print());
    }

    @Test
    public void testCreateDepartment() throws Exception {
        long deptId = 1;
        department = new DepartmentDTO(deptId,
                "HR",
                "Description 1",
                1,
                1,
                new Timestamp(date.getTime()),
                new Timestamp(date.getTime()));

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(department);

        this.mockMvc.perform(post("/departments")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }
    @Test
    public void testCreateDepartmentBadRequest() throws Exception {
        long deptId = 1;
        department = new DepartmentDTO(deptId,
                "",
                "Description 1",
                1,
                1,
                new Timestamp(date.getTime()),
                new Timestamp(date.getTime()));

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(department);

        this.mockMvc.perform(post("/departments")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath(".fieldErrors.deptName")
                        .value("Department name cannot be empty."))
                .andDo(print());
    }

    @Test
    public void testUpdateDepartment() throws Exception {
        long deptId = 1;
        department = new DepartmentDTO(deptId,
                "Name 1",
                "Description 1",
                1,
                1,
                new Timestamp(date.getTime()),
                new Timestamp(date.getTime()));

        when(departmentService.updateDepartment(deptId, department)).thenReturn(department);

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(department);

        this.mockMvc.perform(put("/departments/{deptId}", deptId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    public void testUpdateDepartmentBadRequest() throws Exception {
        long deptId = 1;
        department = new DepartmentDTO(deptId,
                "",
                "Description 1",
                1,
                1,
                new Timestamp(date.getTime()),
                new Timestamp(date.getTime()));

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(department);

        this.mockMvc.perform(put("/departments/{deptId}", deptId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath(".fieldErrors.deptName")
                        .value("Department name cannot be empty."))
                .andDo(print());
    }

    @Test
    public void testDeleteDepartment() throws Exception {
        long deptId = 1;
        department = new DepartmentDTO(deptId,
                "HR",
                "Description 1",
                1,
                1,
                new Timestamp(date.getTime()),
                new Timestamp(date.getTime()));

        when(departmentService.deleteDepartment(deptId)).thenReturn(department);

        this.mockMvc.perform(delete("/departments/{deptId}", deptId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(".deptId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath(".deptName").value("HR"))
                .andExpect(MockMvcResultMatchers.jsonPath(".deptDescription").value("Description 1"))
                .andExpect(MockMvcResultMatchers.jsonPath(".createdBy").value(1))
                .andDo(print());
    }
    @Test
    public void testDeleteDepartmentNotFound() throws Exception {
        long deptId = 1;

        when(departmentService.deleteDepartment(deptId))
                .thenThrow(new DepartmentNotFoundException("Department not found with id : "+deptId));

        this.mockMvc.perform(delete("/departments/{deptId}", deptId))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath(".message")
                        .value("Department not found with id : 1"))
                .andDo(print());
    }


}
