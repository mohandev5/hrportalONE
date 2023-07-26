package com.task.hrPortalOne.repo;

import com.task.hrPortalOne.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface LoginRepo extends JpaRepository<Login,Integer> {


    List<Login> findLogInByEmpIdAndDate(int empId, Optional<Date> date);

    List<Login> findEmpIdAndLogInByDate(Optional<Date> date);

    List<Login> findLoginByEmpId(int empId);
}
