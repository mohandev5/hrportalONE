package com.task.hrPortalOne.repo;

import com.task.hrPortalOne.entity.LogOut;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface LogOutRepo extends JpaRepository<LogOut,Integer> {
    List<LogOut> findLogOutByEmpIdAndDate(int empId, Optional<Date> date);

    List<LogOut> findEmpIdAndLogOutByDate(Optional<Date> date);

    List<LogOut>findLogOutByEmpId(int empId);
}
