package com.credaegis.backend.controller;


import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.http.response.api.CustomApiResponse;
import com.credaegis.backend.service.BlockchainService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = Constants.ROUTEV1+"/web3")
@AllArgsConstructor
public class BlockchainController {

    private final BlockchainService blockchainService;

    @GetMapping("/get-balance")
    public ResponseEntity<CustomApiResponse<Long>> getBalance(){
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(blockchainService.getBalance(),"Account balance",true)
        );
    }


}
