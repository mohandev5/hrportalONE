package com.task.hrPortalOne.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.sql.Date;
import java.sql.Time;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class LoginDto {

    private int empId;

    private Time logIn;

    private Date date;
}
