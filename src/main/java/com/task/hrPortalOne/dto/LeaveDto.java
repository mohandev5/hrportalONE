package com.task.hrPortalOne.dto;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class LeaveDto {

    private int empId;

    private Date date;

    private String status;
}
