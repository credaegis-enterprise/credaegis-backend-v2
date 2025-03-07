package com.credaegis.backend.controller.organization;


import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.http.response.api.CustomApiResponse;
import com.credaegis.backend.service.organization.Web3Service;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = Constants.ROUTE_V1_ORGANIZATION +"/web3")
@AllArgsConstructor
public class Web3Controller {

    private final Web3Service web3Service;

    @GetMapping("/get-balance")
    public ResponseEntity<CustomApiResponse<String>> getBalance(){
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(web3Service.getBalance(),"Account balance in Ethers",true)
        );
    }


}
