package com.credaegis.backend.controller;

import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.dto.ApprovalsInfoDTO;
import com.credaegis.backend.entity.Role;
import com.credaegis.backend.entity.User;
import com.credaegis.backend.http.response.api.CustomApiResponse;
import com.credaegis.backend.repository.RoleRepository;
import com.credaegis.backend.repository.UserRepository;
import com.credaegis.backend.service.InitializerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = Constants.ROUTEV1+"/test")
@AllArgsConstructor
public class InitializerController {

   private final InitializerService initializerService;
   private final RoleRepository roleRepository;
   private final UserRepository userRepository;



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
   public ResponseEntity<CustomApiResponse<List<User>>> check(Authentication authentication){
       CustomUser user = (CustomUser) authentication.getPrincipal();

           RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8083/help";
            ResponseEntity<String> response = restTemplate.getForEntity(url,String.class);
            System.out.println(response.getBody());

      return ResponseEntity.status(HttpStatus.OK).body(new CustomApiResponse<>(
              userRepository.findAll(),"sss",true
      ));
   }

   @PostMapping (path = "/file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("hey")
                                                                         String toParse) throws JsonProcessingException {

       ObjectMapper objectMapper = new ObjectMapper();
       List<ApprovalsInfoDTO> approvalsInfoDTOS = objectMapper.readValue(toParse,new TypeReference<List<ApprovalsInfoDTO>>(){});

       for (ApprovalsInfoDTO approvalsInfoDTO : approvalsInfoDTOS) {
           System.out.println(approvalsInfoDTO.getFileName());
           System.out.println(approvalsInfoDTO.getRecipientEmail());
           System.out.println(approvalsInfoDTO.getFileName());
       }
       return ResponseEntity.status(HttpStatus.OK).body(file.getOriginalFilename());
   }
}
