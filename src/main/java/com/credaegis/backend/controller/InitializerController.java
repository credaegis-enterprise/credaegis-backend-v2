package com.credaegis.backend.controller;

import com.credaegis.backend.entity.Role;
import com.credaegis.backend.entity.User;
import com.credaegis.backend.repository.RoleRepository;
import com.credaegis.backend.service.InitializerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/test")
public class InitializerController {

   @Autowired
   InitializerService initializerService;

   @Autowired
   RoleRepository roleRepository;



   @PostMapping (path = "/add/admin")
    public ResponseEntity<String> addUser(@RequestBody User user){
       initializerService.addUserService(user);
       return ResponseEntity.status(HttpStatus.OK).body("hey");
   }


   @GetMapping (path = "/get/role/{id}")
   public ResponseEntity<Role> getRole(@PathVariable String id){
         Role role = roleRepository.findByUser_id(id);
         System.out.println(role.getRole());
         return ResponseEntity.status(HttpStatus.OK).body(role);
   }
}
