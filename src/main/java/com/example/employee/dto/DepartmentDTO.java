package com.example.employee.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {

    private Long deptId;
    @NotBlank(message = "Department name cannot be empty.")
    private String deptName;
    private String deptDescription;
    private int createdBy;
    private int updatedBy;

    @CreationTimestamp
    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp createdOn;
    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp updatedOn;

}
