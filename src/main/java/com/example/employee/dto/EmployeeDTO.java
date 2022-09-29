package com.example.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private Long employeeId;
    @NotBlank(message = "Employee name should not be empty.")
    private String employeeName;
    @Column
    private String employeeDesignation;
    @Pattern(regexp = "^\\d{10}$", message="Number should contain 10 digits.")
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private DepartmentDTO department;

}
