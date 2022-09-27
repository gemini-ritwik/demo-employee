package com.example.employee.services;

import com.example.employee.exception.DepartmentNotFoundException;
import com.example.employee.exception.NoDataFoundException;
import com.example.employee.models.Department;
import com.example.employee.repository.DepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService{
    Logger LOGGER = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    @Autowired
    private DepartmentRepository departmentRepository;

    /**
     * Fetches all the departments from the database.
     * @return List<Department> This returns all the departments
     * @throws Exception Throws exception when there are no departments in the database
     */
    @Override
    public List<Department> getDepartments() throws Exception{
        LOGGER.trace("Entering method getDepartment...");
        List<Department> departments = new ArrayList<>();

        departmentRepository.findAll().forEach(department -> {
            //Check if record is active
            if(department.isActive() && !department.isDeleted())
                departments.add(department);
        });

        //Check if the list of data is empty
        if (departments.isEmpty()) {
            LOGGER.error("No data found in the department table");
            throw new NoDataFoundException("There is no data in the department table");
        }

        LOGGER.info("Fetched all the departments from the database");
        return departments;
    }

    /**
     * Fetches the department with a specific department id
     * @param deptId Department id of the department to be fetched
     * @return Department Returns the department with deptId
     * @throws Exception Throws exception when the department with given id does not exist
     */
    @Override
    public Department getDepartment(Long deptId) throws Exception{
        LOGGER.trace("Entering the method getDepartment");
        LOGGER.debug("Fetching department from the database with id : " + deptId);

        //getting record from the DB
        Department departmentFromDb = departmentRepository.findById(deptId).orElseThrow(
                () -> {
                    LOGGER.error("Department not found with id : "+deptId);
                    return new DepartmentNotFoundException("Department not found with deptId : "+deptId);
                }
        );

        //Check if the record is active and not deleted
        if(!departmentFromDb.isActive() && departmentFromDb.isDeleted()) {
            LOGGER.error("Department not found with id : "+deptId);
            throw new DepartmentNotFoundException("Department not found with deptId : "+deptId);
        }

        LOGGER.info("Fetched department with id : "+deptId);
        return departmentFromDb;
    }

    /**
     * Updates the details of department with the given department id
     * @param deptId Department id of the department to be updated
     * @param department Department details with which existing department is to be replaced
     * @return  Department Returns the updated department
     * @throws Exception Throws exception when the department to be updated does not exist
     */
    @Override
    public Department updateDepartment(Long deptId, Department department) throws Exception{
        LOGGER.trace("Entering method updateDepartment");

        //getting record from the DB
        Department departmentFromDb = departmentRepository.findById(deptId).orElseThrow(
                () -> {
                    LOGGER.error("Department not found with id : "+deptId);
                    return new DepartmentNotFoundException("Department not found with deptId : "+deptId);
                }
        );

        //Check if the record is active and not deleted
        if(!departmentFromDb.isActive() && departmentFromDb.isDeleted()) {
            LOGGER.error("Department not found with id : " + deptId);
            throw new DepartmentNotFoundException("Department not found with deptId : " + deptId);
        }

        LOGGER.debug("Updating the details of the department with id : "+deptId+" from : "+departmentFromDb +" to : "+department.toString());

        //Updating details
        departmentFromDb.setDeptName(department.getDeptName());
        departmentFromDb.setDeptDescription(department.getDeptDescription());
        departmentFromDb.setUpdatedBy(department.getUpdatedBy());

        //Saving to the database
        departmentRepository.save(departmentFromDb);
        LOGGER.info("Details of department updated with id : "+deptId);

        return departmentFromDb;
    }

    /**
     * Saves a department to the database
     * @param department Department to be saved in database
     */
    @Override
    public void createDepartment(Department department) {
        LOGGER.trace("Entering the method createDepartment.");
        department.setUpdatedBy(department.getCreatedBy());
        department.setActive(true);
        departmentRepository.save(department);
        LOGGER.info("Department created successfully");
    }

    /**
     * Delete the department with the given department id
     * @param deptId Department id of the department to be deleted
     * @return Department Returns the department that has been deleted
     * @throws Exception Throws exception when the department to be deleted does not exist
     */
    @Override
    public Department deleteDepartment(Long deptId) throws Exception{
        LOGGER.trace("Entering the method deleteDepartment.");

        //Getting department from db
        Department dept = departmentRepository.findById(deptId).orElseThrow(
                () -> {
                    LOGGER.error("Department not found with id : "+deptId);
                    return new DepartmentNotFoundException("Department not found with deptId : "+deptId);
                }
        );

        dept.setDeleted(true);
        dept.setActive(false);
        departmentRepository.save(dept);

        LOGGER.info("Department deleted with id : "+deptId);
        return dept;
    }
}
