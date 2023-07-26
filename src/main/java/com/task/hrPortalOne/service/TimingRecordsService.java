package com.task.hrPortalOne.service;


import com.task.hrPortalOne.dto.LeaveDto;
import com.task.hrPortalOne.dto.LogOutDto;
import com.task.hrPortalOne.dto.LoginDto;
import com.task.hrPortalOne.entity.Employee;
import com.task.hrPortalOne.entity.LogOut;
import com.task.hrPortalOne.entity.Login;
import com.task.hrPortalOne.entity.TimingRecords;
import com.task.hrPortalOne.exception.ServerException;
import com.task.hrPortalOne.exception.TimingRecordException;
import com.task.hrPortalOne.exception.TimingRecordNotFoundException;
import com.task.hrPortalOne.repo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.*;
import java.util.*;

@Service
public class TimingRecordsService {
    @Autowired
    private TimingRecordsRepo timingRecordsRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private LoginRepo loginRepo;

    @Autowired
    private LogOutRepo logOutRepo;

    @Autowired
    private SecurityUserRepo securityUserRepo;

    Logger logger = LoggerFactory.getLogger(getClass());


    public String login(LoginDto loginDto) {
        if (loginDto.getDate() == null) {
            throw new TimingRecordException("please provide date");
        }
        if (loginDto.getLogIn() == null) {
            throw new TimingRecordException("please provide loginTime");
        }
        try {
            Login login = new Login();
            login.setEmpId(loginDto.getEmpId());
            login.setLogIn(loginDto.getLogIn());
            login.setDate(loginDto.getDate());
            loginRepo.save(login);
            return "logged in successfully";
        } catch (Exception ex) {
            throw new ServerException("error occurred while login", ex.getMessage());
        }
    }

    public String logout(LogOutDto logOutDto) {
        if (logOutDto.getDate() == null) {
            throw new TimingRecordException("please provide logOutDate");
        }
        if (logOutDto.getLogOut() == null) {
            throw new TimingRecordException("please provide date");
        }
        try {
            LogOut logOut = new LogOut();
            logOut.setEmpId(logOutDto.getEmpId());
            logOut.setLogOut(logOutDto.getLogOut());
            logOut.setDate(logOutDto.getDate());
            logOutRepo.save(logOut);
            return "you logged out successfully";
        } catch (Exception ex) {
            throw new ServerException("error occurred while logout", ex.getMessage());
        }
    }

    public String totalWorkingHoursInADay(int empId, Optional<Date> date) {
//        try {
        List<Login> loginlist = loginRepo.findLogInByEmpIdAndDate(empId, date);
        List<LogOut> logOutList = logOutRepo.findLogOutByEmpIdAndDate(empId, date);
        Duration totalDuration = Duration.ZERO;

        if (empId < 0 || date.isEmpty()) {
            throw new TimingRecordException("please provide date and empId");
        }

        if (loginlist.isEmpty() || logOutList.isEmpty()) {
            throw new TimingRecordNotFoundException("No records found for the given employeeId and date:" + empId + " " + date);
        }
        try {
            for (Login login : loginlist) {
                if (login.getLogIn() == null) {
                    continue;
                }
                Time loginTime = Time.valueOf(login.getLogIn().toLocalTime());
                Duration duration = Duration.ZERO;
                Duration countDuration = Duration.ZERO;
                for (LogOut logOut : logOutList) {
                    if (logOut.getLogOut() != null) {
                        Time logoutTime = Time.valueOf(logOut.getLogOut().toLocalTime());
                        countDuration = Duration.between(loginTime.toLocalTime(), logoutTime.toLocalTime());
                    }
                }
                totalDuration = totalDuration.plus(countDuration);
                break;
            }
            long totalSeconds = totalDuration.getSeconds();
            long hours = totalSeconds / 3600;
            long minutes = (totalSeconds % 3600) / 60;
            long seconds = totalSeconds % 60;
            return "hours: " + hours + ", minutes: " + minutes + ", seconds: " + seconds;
        } catch (Exception ex) {
            throw new ServerException("error occurred while calculating total working hours", ex.getMessage());
        }

    }


    public String leaveApply(LeaveDto leaveDto) {
        if (leaveDto.getStatus().isEmpty()) {
            throw new TimingRecordException("please provide leave status");
        }
        if (leaveDto.getDate() == null) {
            throw new TimingRecordException("please provide date");
        }
        try {
            Login login = new Login();
            login.setEmpId(leaveDto.getEmpId());
            login.setDate((java.sql.Date) leaveDto.getDate());
            login.setStatus(leaveDto.getStatus());
            loginRepo.save(login);
            return "You are applied for a leave on:" + " " + leaveDto.getDate();
        } catch (Exception ex) {
            throw new ServerException("Error in server", ex.getMessage());
        }
    }

    public String leaveApproval(int empId, Date date) {
        List<TimingRecords> timingRecordsList = timingRecordsRepo.findStatusByEmployeeEmpIdAndDate(empId, date);
        if (timingRecordsList.isEmpty()) {
            throw new TimingRecordNotFoundException("leave list is empty");
        }
        if (date == null) {
            throw new TimingRecordException(("please provide date"));
        }
        for (TimingRecords timingRecords : timingRecordsList) {
            if (timingRecords.getStatus() != null && timingRecords.getStatus().equalsIgnoreCase("applied for a leave")) {
                timingRecords.setStatus("approved");
                timingRecordsRepo.save(timingRecords);
                break;
            } else {
                return "empId:" + " " + empId + "is not applying leave on date:" + " " + date;
            }
        }
        return "Congratulations your leave approved";
    }


    public String leaveRejected(int empId, Date date) {
        List<TimingRecords> timingRecordsList = timingRecordsRepo.findStatusByEmployeeEmpIdAndDate(empId, date);
        if (timingRecordsList.isEmpty()) {
            throw new TimingRecordNotFoundException("leave list is empty");
        }
        for (TimingRecords timingRecords : timingRecordsList) {
            if (timingRecords.getStatus() != null && timingRecords.getStatus().equalsIgnoreCase("applied for a leave")) {
                timingRecords.setStatus("rejected");
                timingRecordsRepo.save(timingRecords);
                break;
            } else {
                return "empId:" + " " + empId + "is not applying leave on date:" + " " + date;
            }
        }
        return "sorry your leave rejected";
    }


    public List<Employee> moreThan8HoursWork(Date date) {
//        try {
        List<Login> logins = loginRepo.findEmpIdAndLogInByDate(Optional.ofNullable(date));
        List<LogOut> logOuts = logOutRepo.findEmpIdAndLogOutByDate(Optional.ofNullable(date));
        List<Employee> employeesWithMoreThan8HoursWork = new ArrayList<>();
        if (date == null) {
            throw new TimingRecordException("Please provide proper date");
        }
        if (logins.isEmpty() || logOuts.isEmpty()) {
            throw new TimingRecordNotFoundException("no one was working on this date:" + date);
        }
        try {
            for (Login login : logins) {
                int loginEmpId = login.getEmpId();
                Time loginTime = login.getLogIn();
                if (loginTime != null) {
                    for (LogOut logOut : logOuts) {
                        int logoutEmpId = logOut.getEmpId();
                        if (loginEmpId == logoutEmpId) {
                            Time logoutTime = logOut.getLogOut();
                            if (logoutTime != null) {
                                Duration duration = Duration.between(loginTime.toLocalTime(), logoutTime.toLocalTime());
                                if (duration.compareTo(Duration.ofHours(8)) > 0) {
                                    Employee employee = employeeRepo.findById(loginEmpId).orElse(null);
                                    if (employee != null) {
                                        employeesWithMoreThan8HoursWork.add(employee);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (RuntimeException ex) {
            throw new ServerException("error in server layer", ex.getMessage());
        }
        return employeesWithMoreThan8HoursWork;
    }


    public String calculateNumberOfLeaves(String name) {
        List<TimingRecords> timingRecordsList = timingRecordsRepo.findStatusByEmployeeName(name);
        try {
            if(name.isEmpty()){
                throw new TimingRecordException("please provide name");
            }
            if (timingRecordsList.isEmpty()) {
                throw new TimingRecordNotFoundException("list is empty for" + name);
            }
            int leaveCount = 0;
            for (TimingRecords timingRecords : timingRecordsList) {
                if (timingRecords.getStatus() != null && timingRecords.getStatus().equalsIgnoreCase("approved")) {
                    leaveCount++;
                }
            }
            return "number of leaves by" + " " + name + " " + "is:" + " " + leaveCount;
        } catch (TimingRecordException ex) {
            throw new TimingRecordNotFoundException("please provide the name");
        }catch (ServerException ex){
            throw new ServerException("Internal server error",ex.getMessage());
        }
    }

//    public double findAggregatePercentageWork(String name){
//        List<TimingRecords> timingRecordsList = timingRecordsRepo.findStatusByEmployeeName(name);
//        Duration totalDuration = Duration.ZERO;
//        Duration totalWorkingHours = Duration.ofHours(100);
//        double aggregateWorkingHours = 0;
//        for(TimingRecords timingRecords:timingRecordsList){
//            Time logInTime = Time.valueOf(timingRecords.getLogIn());
//            Time logOutTime = Time.valueOf(timingRecords.getLogOut());
//            if(logInTime.toLocalTime()==null&&logOutTime.toLocalTime()==null){
//                continue;
//            }
//            Duration duration = Duration.between(logInTime.toLocalTime(),logOutTime.toLocalTime());
//            totalDuration=duration.plus(totalDuration);
//        }
//        aggregateWorkingHours = (totalDuration.dividedBy(totalWorkingHours))*100;
//        return aggregateWorkingHours;
//    }
}




