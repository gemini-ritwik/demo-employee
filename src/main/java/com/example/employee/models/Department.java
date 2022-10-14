package com.example.employee.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "department")
public class Department {

    @Id
    @SequenceGenerator(
            name = "department_sequence",
            sequenceName = "department_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "department_sequence"
    )
    @Column(
            name = "dept_id"
    )
    private Long deptId;
    @NotBlank(message = "Department name cannot be empty.")
    private String deptName;
    private String deptDescription;
    private int createdBy;
    private int updatedBy;
    private boolean isActive;
    private boolean isDeleted;

    @CreationTimestamp
    @Column(updatable = false)
    Timestamp createdOn;
    @UpdateTimestamp
    Timestamp updatedOn;

    @OneToMany(
            mappedBy = "department"
    )
    private Set<Employee> employees = new HashSet<>();

    public Department(String deptName, String deptDescription, int createdBy) {
        this.deptName = deptName;
        this.deptDescription = deptDescription;
        this.createdBy = createdBy;
        this.updatedBy = createdBy;
        this.isActive = true;
        this.isDeleted = false;
    }

    public Department(String deptName, String deptDescription,int createdBy, int updatedBy) {
        this.deptName = deptName;
        this.deptDescription = deptDescription;
        this.updatedBy = updatedBy;
        this.isActive = true;
        this.isDeleted = false;
    }

    public Department(Long deptId, String deptName, String deptDescription, int createdBy, int updatedBy, boolean isActive, boolean isDeleted, Set<Employee> employees) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.deptDescription = deptDescription;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.employees = employees;
    }

    @Override
    public String toString() {
        return "Department{" +
                "deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                ", deptDescription='" + deptDescription + '\'' +
                ", createdOn=" + createdOn +
                ", createdBy=" + createdBy +
                ", updatedOn=" + updatedOn +
                ", updatedBy=" + updatedBy +
                ", isActive=" + isActive +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
