package com.task.hrPortalOne.controller;

import com.task.hrPortalOne.entity.SecurityUser;
import com.task.hrPortalOne.service.SecurityUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hrPortal")
public class SecurityUserController {

    @Autowired
    private SecurityUserService securityUserService;

    @PostMapping("/addSecurityUsers")
    public ResponseEntity<String> addSecurity(@RequestBody SecurityUser securityUser)  {
        return ResponseEntity.ok(securityUserService.addNewUser(securityUser));
    }

}
