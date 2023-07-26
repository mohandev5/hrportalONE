package com.task.hrPortalOne.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "timings")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class LogOut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timing_id")
    private int timingId;

    @Column(name = "emp_id")
    private int empId;

    @Column(name = "log_out")
    private Time logOut;

    @Column(name = "date")
    private Date date;
}
