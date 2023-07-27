package com.task.hrPortalOne.advice;

import com.task.hrPortalOne.exception.*;
import com.task.hrPortalOne.exception.NumberFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;
import java.util.List;
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TimingRecordNotFoundException.class)
    public ProblemDetail timingRecordNotFound(RuntimeException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setType(URI.create("https://api.timingrecords.com/errors/not-found"));
        pd.setStatus(HttpStatus.NOT_FOUND);
        pd.setTitle(pd.getTitle());
        pd.setProperty("errorCategory","Generic");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ProblemDetail employeeNotFound(EmployeeNotFoundException ex){
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setType(URI.create("https://api.employee.com/errors/not-found"));
        pd.setStatus(HttpStatus.NOT_FOUND);
        pd.setTitle(pd.getTitle());
        pd.setProperty("errorCategory","Generic");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }
    @ExceptionHandler(EmployeeException.class)
    public ProblemDetail employeeException(EmployeeException ex){
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        pd.setType(URI.create("https://api.employee.com/errors/bad_request"));
        pd.setStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle(pd.getTitle());
        pd.setProperty("errorCategory","Generic");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(TimingRecordException.class)
    public ProblemDetail timingRecordException(EmployeeException ex){
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        pd.setType(URI.create("https://api.TimingRecords.com/errors/bad_request"));
        pd.setStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle(pd.getTitle());
        pd.setProperty("errorCategory","Generic");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(ServerException.class)
    public ProblemDetail handleServerError(ServerException ex){
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setType(URI.create("https://api.employee.com/errors/Internal_server_error"));
        pd.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle(pd.getTitle());
        pd.setProperty("errorCategory","Generic");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    public ProblemDetail handleNumbers(NumberFormatException ex){
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setType(URI.create("https://api.employee.com/errors/bad_request"));
        pd.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle(pd.getTitle());
        pd.setProperty("errorCategory","Generic");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }
}
