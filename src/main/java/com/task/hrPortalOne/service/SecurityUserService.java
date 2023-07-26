package com.task.hrPortalOne.service;

import com.task.hrPortalOne.entity.SecurityUser;
import com.task.hrPortalOne.repo.SecurityUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class SecurityUserService  {
    @Autowired
    private SecurityUserRepo securityUserRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    public String addNewUser(SecurityUser user)  {
            String encodePassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodePassword);
            securityUserRepo.save(user);
            return "user created successfully";

    }
}
