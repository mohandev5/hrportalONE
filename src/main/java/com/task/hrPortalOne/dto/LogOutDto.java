package com.task.hrPortalOne.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.sql.Date;
import java.sql.Time;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class LogOutDto {

    private int empId;

    private Time logOut;

    private Date date;
}
