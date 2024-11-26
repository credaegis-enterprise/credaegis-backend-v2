package com.credaegis.backend.controller;

import com.credaegis.backend.entity.Users;
import com.credaegis.backend.repository.UsersRepository;
import com.credaegis.backend.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/test")
public class testController {

   @Autowired
   TestService testService;



   @PostMapping (path = "/add/admin")
    public ResponseEntity<String> addUser(@RequestBody Users user){
       testService.addUserService(user);
       return ResponseEntity.status(HttpStatus.OK).body("hey");
   }
}
