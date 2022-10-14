package com.example.employee.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee")
public class Employee {

    @Id
    @SequenceGenerator(
            name = "employee_sequence",
            sequenceName = "employee_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "employee_sequence"
    )
    private Long employeeId;
    @NotBlank(message = "Employee name should not be empty.")
    private String employeeName;

    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "address_id",
            referencedColumnName = "addressId"
    )
    private Address employeeAddress;
    @Column
    private String employeeDesignation;

    @Pattern(regexp = "^\\d{10}$", message="Number should contain 10 digits.")
    private String phoneNumber;
    @Column(
            name = "is_active",
            nullable = false
    )
    private boolean isActive;
    @Column(
            name = "is_deleted",
            nullable = false
    )
    private boolean isDeleted;

    @ManyToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "dept_id",
            referencedColumnName = "dept_id"
    )
    private Department department;

    public Employee(String employeeName, Address employeeAddress, String employeeDesignation, String phoneNumber) {
        this.employeeName = employeeName;
        this.employeeAddress = employeeAddress;
        this.employeeDesignation = employeeDesignation;
        this.phoneNumber = phoneNumber;
        this.isActive = true;
        this.isDeleted = false;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", employeeAddress=" + employeeAddress +
                ", employeeDesignation='" + employeeDesignation + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isActive=" + isActive +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
