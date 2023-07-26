package com.task.hrPortalOne.repo;

import com.task.hrPortalOne.entity.Employee;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepo extends JpaRepository<Employee,Integer> {

    //    void deleteById(int empId);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Employee e WHERE e.name = :name AND e.empId = :empId",nativeQuery = true)
    void deleteByNameAndId(String name, int empId);

    List<Employee> findEmployeeByNameAndEmpId(String name, int empId);
}
