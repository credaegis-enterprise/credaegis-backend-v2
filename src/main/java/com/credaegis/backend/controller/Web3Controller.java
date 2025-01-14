package com.credaegis.backend.controller;


import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.dto.ContractStateDTO;
import com.credaegis.backend.dto.HashBatchInfoDTO;
import com.credaegis.backend.http.response.api.CustomApiResponse;
import com.credaegis.backend.service.Web3Service;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = Constants.ROUTEV1+"/web3")
@AllArgsConstructor
public class Web3Controller {

    private final Web3Service web3Service;



    @GetMapping("/besu/batch/{id}")
    public ResponseEntity<CustomApiResponse<HashBatchInfoDTO>> getBatchInfo(@PathVariable  String id){
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(web3Service.getBatchInfo(id),"Batch info",true)
        );
    }

    @GetMapping("/get-balance")
    public ResponseEntity<CustomApiResponse<String>> getBalance(){
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(web3Service.getBalance(),"Account balance in Ethers (Avalanche)",true)
        );
    }


    @GetMapping("/besu/current-batch")
    public ResponseEntity<CustomApiResponse<HashBatchInfoDTO>> getCurrentBatchInfo(){

        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(web3Service.getCurrentBatchInfo(),"Current batch info",true)
        );
    }


    @GetMapping("/besu/contract-state")
    public ResponseEntity<CustomApiResponse<ContractStateDTO>> getContractState(){

        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(     web3Service.getContractState(),"Contract state",true)
        );
    }


}
