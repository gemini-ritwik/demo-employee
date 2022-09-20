package com.example.employee.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
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


    public Employee() {
    }

    public Employee(String employeeName, Address employeeAddress, String employeeDesignation, String phoneNumber) {
        this.employeeName = employeeName;
        this.employeeAddress = employeeAddress;
        this.employeeDesignation = employeeDesignation;
        this.phoneNumber = phoneNumber;
        this.isActive = true;
        this.isDeleted = false;
    }

    public Employee(Long employeeId, String employeeName, Address employeeAddress, String employeeDesignation, String phoneNumber, boolean isActive, boolean isDeleted, Department department) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeAddress = employeeAddress;
        this.employeeDesignation = employeeDesignation;
        this.phoneNumber = phoneNumber;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.department = department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setEmployeeAddress(Address employeeAddress) {
        this.employeeAddress = employeeAddress;
    }

    public void setEmployeeDesignation(String employeeDesignation) {
        this.employeeDesignation = employeeDesignation;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public Address getEmployeeAddress() {
        return employeeAddress;
    }

    public String getEmployeeDesignation() {
        return employeeDesignation;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public Department getDepartment() {
        return department;
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
