package com.task.hrPortalOne.service;


import com.task.hrPortalOne.dto.EmployeeDto;
import com.task.hrPortalOne.entity.Employee;
import com.task.hrPortalOne.exception.EmployeeException;
import com.task.hrPortalOne.exception.EmployeeNotFoundException;
import com.task.hrPortalOne.exception.NumberFormatException;
import com.task.hrPortalOne.exception.ServerException;
import com.task.hrPortalOne.repo.EmployeeRepo;
import com.task.hrPortalOne.repo.SecurityUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class EmployeeService  {

    @Autowired
    public EmployeeRepo employeeRepo;

    @Autowired
    public SecurityUserRepo securityUserRepo;


    public List<Employee> employeeList()  {
        try {
            List<Employee> employeeList = employeeRepo.findAll();
            if (employeeList.isEmpty()) {
                throw new EmployeeNotFoundException("the employee list is completely empty");
            }
            return employeeList;
        }catch (Exception ex){
            throw new ServerException("someThing went wrong in service layer while retrieving the employeeList"+ex.getMessage(), ex.getMessage());
        }
    }

    public Page<Employee> getAllDetails(int pageNumber, int pageSize, String sortAttribute)  {
        try {
            if (sortAttribute.isEmpty()) {
                throw new EmployeeException("Please provide the required properties: pageNumber, pageSize, and sortAttribute.");
            }
            List<String> allowedSortAttributes = Arrays.asList("name", "email", "designation");
            if (!allowedSortAttributes.contains(sortAttribute)) {
                throw new EmployeeException("Invalid sort attribute. Allowed attributes: name, email, designation");
            }
                Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortAttribute));
                return employeeRepo.findAll(pageable);
            } catch (NumberFormatException ex) {
                throw new NumberFormatException("please provide pageNumber and PageSize properly");
            } catch (Exception ex){
            throw new ServerException("error in employee service", ex.getMessage());
        }
    }


    public String addNewEmployee(EmployeeDto employeeDto)  {
        if (employeeDto.getName() == null || employeeDto.getName().isEmpty()) {
            throw new EmployeeException("Please fill in a proper name while adding");
        }

        if (employeeDto.getEmail() == null || employeeDto.getEmail().isEmpty()) {
            throw new EmployeeException("Please provide a proper email");
        }

        if (employeeDto.getDesignation() == null || employeeDto.getDesignation().isEmpty()) {
            throw new EmployeeException("Please provide a proper designation");
        }

        try {
            Employee employee = new Employee();
            employee.setName(employeeDto.getName());
            employee.setEmail(employeeDto.getEmail());
            employee.setDesignation(employeeDto.getDesignation());
            employeeRepo.save(employee);
            return "New employee added";
        } catch (Exception ex) {
            throw new ServerException("Something went wrong in the service layer: " + ex.getMessage(), ex.getMessage());
        }
    }


    public String removeAnEmployee(String name, int empId)  {
        if (name.isEmpty()) {
            throw new EmployeeException("Please provide a name");
        }
        List<Employee> employeeList = employeeRepo.findEmployeeByNameAndEmpId(name, empId);
        if (employeeList.isEmpty()) {
            throw new EmployeeNotFoundException("No employee found with the name and empId: " + name + " " + empId);
        }
        try {
            employeeRepo.deleteByNameAndId(name, empId);
            return "Employee removed successfully";
        } catch (Exception ex) {
            throw new ServerException("Error in EmployeeService layer: " + ex.getMessage(), ex.getMessage());
        }
    }

}
