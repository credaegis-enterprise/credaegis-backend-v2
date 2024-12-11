package com.credaegis.backend.controller;

import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.dto.FileMetaInfoDTO;
import com.credaegis.backend.entity.Role;
import com.credaegis.backend.repository.RoleRepository;
import com.credaegis.backend.service.InitializerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = Constants.ROUTEV1+"/test")
@AllArgsConstructor
public class InitializerController {


   private final InitializerService initializerService;
   private final RoleRepository roleRepository;



   @PostMapping (path = "/add/organization")
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

      return ResponseEntity.status(HttpStatus.OK).body("OK CHECKED");
   }

   @PostMapping (path = "/file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("hey")
                                                                         String toParse) throws JsonProcessingException {

       ObjectMapper objectMapper = new ObjectMapper();
       List<FileMetaInfoDTO> fileMetaInfoDTOS = objectMapper.readValue(toParse,new TypeReference<List<FileMetaInfoDTO>>(){});

       for (FileMetaInfoDTO fileMetaInfoDTO : fileMetaInfoDTOS) {
           System.out.println(fileMetaInfoDTO.getFileName());
           System.out.println(fileMetaInfoDTO.getRecipientEmail());
           System.out.println(fileMetaInfoDTO.getFileName());
       }
       return ResponseEntity.status(HttpStatus.OK).body(file.getOriginalFilename());
   }
}
