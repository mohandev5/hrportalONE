package com.task.hrPortalOne.controller;


import com.task.hrPortalOne.dto.LeaveDto;
import com.task.hrPortalOne.dto.LogOutDto;
import com.task.hrPortalOne.dto.LoginDto;
import com.task.hrPortalOne.repo.LogOutRepo;
import com.task.hrPortalOne.service.TimingRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;


@RestController
//@Slf4j
@RequestMapping("/hrPortal")
public class TimingRecordController {


    //    Logger logger = LoggerFactory.getLogger(TimingRecordController.class);
    @Autowired
    private TimingRecordsService timingRecordsService;

    @Autowired
    private LogOutRepo logOutRepo;

    @PostMapping("/loggedIn")
    public ResponseEntity<String> loggedIn(@RequestBody LoginDto login)  {
        return ResponseEntity.ok(timingRecordsService.login(login));
    }

    @PostMapping("/loggedOut")
    public ResponseEntity<String> loggedOut(@RequestBody LogOutDto logOut)  {
        return ResponseEntity.ok(timingRecordsService.logout(logOut));
    }

    @PreAuthorize("hasAnyAuthority('HR','MANAGER')")
    @GetMapping("/totalWork")
    public ResponseEntity<Object> totalWorkInADay(
            @RequestParam("empId")int empId,
            @RequestParam("date")@DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> date) {
        return  ResponseEntity.ok(timingRecordsService.totalWorkingHoursInADay(empId, date));
//        return ResponseHandler.responseBuilder("total workingHours of empId:"+" "+empId+"on date:"+" "+date,HttpStatus.OK,timingRecordsService.totalWorkingHoursInADay(empId, date));
    }

    @PostMapping("/leaveApplication")
    public ResponseEntity<String> appliedForALeave(@RequestBody LeaveDto leaveDto)  {
        return ResponseEntity.ok(timingRecordsService.leaveApply(leaveDto));
    }


    @PreAuthorize("hasAnyAuthority('HR','MANAGER')")
    @PutMapping("/approval")
    public ResponseEntity<String> Approval(
            @RequestParam("empId")int empId,
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date)  {
        return ResponseEntity.ok(timingRecordsService.leaveApproval(empId, date));
    }

    @PreAuthorize("hasAnyAuthority('HR','MANAGER')")
    @PutMapping("/rejected")
    public ResponseEntity<String> rejected(@RequestParam("empId")int empId,
                                           @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd")Date date) {
        return ResponseEntity.ok(timingRecordsService.leaveRejected(empId, date));
    }

    @PreAuthorize("hasAnyAuthority('HR','MANAGER')")
    @GetMapping("/employeesWithMoreThan8Hours")
    public ResponseEntity<Object> getEmployeesWorkMoreThan8Hours(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date)  {
        return ResponseEntity.ok(timingRecordsService.moreThan8HoursWork(date));
    }
    @PreAuthorize("hasAnyAuthority('HR','MANAGER')")
    @GetMapping("/countLeavesOfAnEmployee")
    public String countLeaves(@RequestParam ("name")String name){
        return timingRecordsService.calculateNumberOfLeaves(name);
    }

//    @GetMapping("/percentage")
//    public double percentageOfWork(@RequestParam("name")String name){
//        return timingRecordsService.findAggregatePercentageWork(name);
//    }
}
