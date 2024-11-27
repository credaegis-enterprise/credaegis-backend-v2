package com.credaegis.backend.controller;

import com.credaegis.backend.Constants;
import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.entity.Role;
import com.credaegis.backend.entity.User;
import com.credaegis.backend.repository.RoleRepository;
import com.credaegis.backend.service.InitializerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = Constants.ROUTEV1+"/test")
public class InitializerController {

   @Autowired
   InitializerService initializerService;

   @Autowired
   RoleRepository roleRepository;



   @PostMapping (path = "/add/admin")
    public ResponseEntity<String> addUser(){
       initializerService.addUserService();
       return ResponseEntity.status(HttpStatus.OK).body("hey");
   }


   @GetMapping (path = "/get/role/{id}")
   public ResponseEntity<Role> getRole(@PathVariable String id){
         Role role = roleRepository.findByUser_id(id);
         System.out.println(role.getRole());
         return ResponseEntity.status(HttpStatus.OK).body(role);
   }

   @PostMapping (path = "/check")
   public ResponseEntity<String> check(Authentication authentication){
       CustomUser user = (CustomUser) authentication.getPrincipal();
       System.out.println(user.getOrganizationId());
       System.out.println(user.getPassword());
       System.out.println(user.getUsername());
      return ResponseEntity.status(HttpStatus.OK).body("OK CHECKED");
   }
}
