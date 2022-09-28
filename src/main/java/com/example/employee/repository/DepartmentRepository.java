package com.example.employee.repository;

import com.example.employee.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByIsActiveAndIsDeleted(boolean isActive, boolean isDeleted);
}
