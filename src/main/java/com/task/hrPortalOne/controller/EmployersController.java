package com.task.hrPortalOne.controller;

import com.task.hrPortalOne.entity.Employee;
import com.task.hrPortalOne.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

@RestController
public class EmployersController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/sorting")
    public ResponseEntity<?> sortingAllEmployees(@RequestParam("pageNumber") @Min(value = 1, message = "pageNumber must be greater than or equal to 1") int pageNumber,
                                                 @RequestParam("pageSize")@Min(value = 1, message = "PageSize must be greater than or equal to 1")   int pageSize,
                                                 @RequestParam("sortAttribute") String sortAttribute) {
            return ResponseEntity.ok((Page<Employee>) employeeService.getAllDetails(pageNumber, pageSize, sortAttribute));
    }

    @ExceptionHandler(NumberFormatException.class)
    public String handleNumberFormatException(NumberFormatException ex) {
        return "please provide pageNumber and pageSize properly";
    }
}
