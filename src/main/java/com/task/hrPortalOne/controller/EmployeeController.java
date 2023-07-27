package com.task.hrPortalOne.controller;

import com.task.hrPortalOne.dto.EmployeeDto;
import com.task.hrPortalOne.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
//@Slf4j
@RequestMapping("/hrPortal")
public class EmployeeController {


//    Logger logger = (Logger) LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/listOfAllEmployees")
    public ResponseEntity<Object> getAll()  {
        return ResponseEntity.ok(employeeService.employeeList());
    }
    @GetMapping("/sorting")
    public ResponseEntity<Object> sortingAllEmployees(@RequestParam("pageNumber") int pageNumber,
                                                      @RequestParam("pageSize") int pageSize,
                                                      @RequestParam("sortAttribute") String sortAttribute)  {
        return ResponseEntity.ok(employeeService.getAllDetails(pageNumber, pageSize, sortAttribute));
    }
     @PreAuthorize("hasAuthority('HR')")
     @PostMapping("addNew")
     public ResponseEntity<?> addEmployee(@RequestBody EmployeeDto employee)  {
        return ResponseEntity.ok(employeeService.addNewEmployee(employee));
     }
    @DeleteMapping("/remove")
    public ResponseEntity<?> removeAnEmployee(@RequestParam(name = "empId",required = true)int empId,
                                              @RequestParam(name = "name")String name){
            return ResponseEntity.ok(employeeService.removeAnEmployee(name, empId));
    }
//    @ExceptionHandler(NumberFormatException.class)
//    public String handleNumberFormatException(NumberFormatException ex) {
//        return "please provide pageNumber and pageSize properly";
//    }
}
