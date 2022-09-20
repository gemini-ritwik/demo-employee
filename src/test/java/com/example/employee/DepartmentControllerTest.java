package com.example.employee;

import com.example.employee.controller.DepartmentController;
import com.example.employee.models.Department;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    List<Department> departments;
    Department department;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();
    }

    @Test
    public void testGetDepartments() throws Exception {
        departments = new ArrayList<>();
        departments.add(new Department(1L,"HR", "Description 1",1, 1, true, false, null));
        departments.add(new Department(2L, "DevOps", "Description 2",2,2,true, false, null));
        departments.add(new Department(3L, "Design", "Description 3",1, 1, true, false, null));

        when(departmentService.getDepartments()).thenReturn(departments);

        this.mockMvc.perform(get("/departments"))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    public void testGetDepartment() throws Exception {
        long deptId = 1;
        department = new Department(deptId, "HR", "Description 1", 1, 1, true, false, null);

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
    public void testCreateDepartment() throws Exception {
        department = new Department("HR", "Description 1", 1);

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(department);

        this.mockMvc.perform(post("/departments")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public void testUpdateDepartment() throws Exception {
        long deptId = 1;
        department = new Department(deptId, "HR", "Description 1", 1, 1, true, false, null);

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
    public void testDeleteDepartment() throws Exception {
        long deptId = 1;
        department = new Department(deptId, "HR", "Description 1", 1, 1, true, false, null);

        when(departmentService.deleteDepartment(deptId)).thenReturn(department);

        this.mockMvc.perform(delete("/departments/{deptId}", deptId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(".deptId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath(".deptName").value("HR"))
                .andExpect(MockMvcResultMatchers.jsonPath(".deptDescription").value("Description 1"))
                .andExpect(MockMvcResultMatchers.jsonPath(".createdBy").value(1))
                .andDo(print());
    }
}
