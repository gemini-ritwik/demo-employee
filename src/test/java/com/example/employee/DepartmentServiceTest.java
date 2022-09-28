package com.example.employee;

import com.example.employee.dto.DepartmentDTO;
import com.example.employee.exception.DepartmentNotFoundException;
import com.example.employee.exception.NoDataFoundException;
import com.example.employee.models.Department;
import com.example.employee.repository.DepartmentRepository;
import com.example.employee.services.DepartmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {DepartmentServiceTest.class})
public class DepartmentServiceTest {

    @Mock
    DepartmentRepository departmentRepository;

    @InjectMocks
    DepartmentServiceImpl departmentService;

    public List<Department> myDepartments;

    private final ModelMapper modelMapper = new ModelMapper();

    @Test
    public void testGetDepartments() throws Exception {

        myDepartments = new ArrayList<>();
        myDepartments.add(new Department(1L,
                "HR",
                "Description 1",
                1,
                1,
                true,
                false,
                null));
        myDepartments.add(new Department(2L,
                "DevOps",
                "Description 2",
                2,
                2,
                true,
                false,
                null));
        myDepartments.add(new Department(3L,
                "Design",
                "Description 3",
                1,
                1,
                true,
                false,
                null));

        when(departmentRepository.findByIsActiveAndIsDeleted(true, false))
                .thenReturn(myDepartments);

        assertEquals(3, departmentService.getDepartments().size());
    }
    @Test
    public void testGetDepartmentThrowsNoDataFoundException() throws Exception {

        myDepartments = new ArrayList<>();

        when(departmentRepository.findByIsActiveAndIsDeleted(true, false))
                .thenReturn(myDepartments);

        assertThatThrownBy(() -> departmentService.getDepartments())
                .isInstanceOf(NoDataFoundException.class);
    }

    @Test
    public void testGetDepartment() throws Exception {

        long id = 1;
        Department myDepartment = new Department(id, "HR", "Description 1", 1, 1, true, false, null);

        when(departmentRepository.findById(id)).thenReturn(Optional.of(myDepartment));

        assertEquals(Long.valueOf(id), departmentService.getDepartment(id).getDeptId());
        assertEquals(myDepartment.getDeptName(), departmentService.getDepartment(id).getDeptName());
        assertEquals(myDepartment.getDeptDescription(), departmentService.getDepartment(id).getDeptDescription());
        assertEquals(myDepartment.getCreatedBy(), departmentService.getDepartment(id).getCreatedBy());
    }
    @Test
    public void testGetDepartmentThrowsDepartmentNotFoundException() throws Exception {
        long id = 1;

        when(departmentRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentService.getDepartment(id))
                .isInstanceOf(DepartmentNotFoundException.class);
    }

    @Test
    public void testCreateDepartment() {

        long id = 1;
        Department department = new Department(id,
                "HR",
                "Description 1",
                1,
                1,
                true,
                false,
                null);

        when(departmentRepository.save(department)).thenReturn(department);

        DepartmentDTO departmentDTO = departmentToDepartmentDTO(department);

        departmentService.createDepartment(departmentDTO);

        verify(departmentRepository, times(1))
                .save(argThat(argument -> argument.getDeptId().equals(department.getDeptId())
                        && argument.getDeptName().equals(department.getDeptName())
                        && argument.getDeptDescription().equals(department.getDeptDescription())
                        && argument.getCreatedBy() == department.getCreatedBy()
                        && argument.getUpdatedBy() == department.getUpdatedBy()
                        && argument.isActive() == department.isActive()
                        && argument.isDeleted() == department.isDeleted()));
    }

    @Test
    public void testUpdateDepartment() throws Exception {

        long id = 1;
        Department oldDepartment = new Department(id, "HR", "Description 1", 1, 1, true, false, null);
        Department newDepartment = new Department("DevOps", "Description 2", 2, 2);

        when(departmentRepository.findById(id))
                .thenReturn(Optional.of(oldDepartment));

        DepartmentDTO updatedDepartment = departmentService
                .updateDepartment(id, departmentToDepartmentDTO(newDepartment));

        assertEquals(id, updatedDepartment.getDeptId());
        assertEquals(newDepartment.getDeptName(), updatedDepartment.getDeptName());
        assertEquals(newDepartment.getDeptDescription(), updatedDepartment.getDeptDescription());
        assertEquals(newDepartment.getUpdatedBy(), updatedDepartment.getUpdatedBy());
        assertEquals(1, updatedDepartment.getCreatedBy());
    }
    @Test
    public void testUpdateDepartmentThrowsDepartmentNotFoundException() {
        long id = 1;
        Department newDepartment = new Department("DevOps", "Description 2", 2, 2);

        when(departmentRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentService
                .updateDepartment(id, departmentToDepartmentDTO(newDepartment)))
                .isInstanceOf(DepartmentNotFoundException.class);
    }

    @Test
    public void testDeleteDepartment() throws Exception {
        long id = 1;
        Department department = new Department(id, "HR", "Description 1", 1, 1, true, false, null);

        when(departmentRepository.findById(id)).thenReturn(Optional.of(department));

        departmentService.deleteDepartment(id);

        verify(departmentRepository, times(1)).save(department);
        assertTrue(department.isDeleted());
        assertFalse(department.isActive());
    }
    @Test
    public void testDeleteDepartmentThrowsDepartmentNotFoundException() {
        long id = 1;

        when(departmentRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentService.deleteDepartment(id))
                .isInstanceOf(DepartmentNotFoundException.class);
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
}
