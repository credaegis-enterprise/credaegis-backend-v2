package com.credaegis.backend.controller;


import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.dto.ContractStateDTO;
import com.credaegis.backend.dto.HashBatchInfoDTO;
import com.credaegis.backend.http.request.MerkleRootVerificationRequest;
import com.credaegis.backend.http.response.api.CustomApiResponse;
import com.credaegis.backend.http.response.custom.BlockchainInfoResponse;
import com.credaegis.backend.service.Web3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = Constants.ROUTEV1+"/web3")
@RequiredArgsConstructor
public class Web3Controller {

    private final Web3Service web3Service;




    @GetMapping("/public/txn/{hash}")
    public ResponseEntity<CustomApiResponse<String>> getTransactionReceipt(@PathVariable String hash){
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(web3Service.getTxnDetails(hash),"Transaction receipt fetched",true)
        );
    }
    @PostMapping("/public/store/current/merkle-root")
    public ResponseEntity<CustomApiResponse<Void>> storeCurrentMerkleRootToPublic(){
       web3Service.storeCurrentBatchMerkleRootToPublic();
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null,"Merkle root stored to public blockchain",true)
        );
    }


    @GetMapping("/public/verify/merkle-roots")
    public ResponseEntity<CustomApiResponse<Map<String,Boolean>>> verifyMerkleRootPublic(@RequestBody MerkleRootVerificationRequest merkleRoots){

        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(       web3Service.verifyMerkleRootPublic(merkleRoots.getMerkleRoots()),"Merkle root verified",true)
        );
    }



    @GetMapping("/private/current-batch/merkle-root")
    public ResponseEntity<CustomApiResponse<String>> getMerkleRootCurrentBatch(){

        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(web3Service.getCurrentBatchMerkleRoot(),"Merkle root of current batch",true)
        );
    }


    @GetMapping("/public/info")
    public ResponseEntity<CustomApiResponse<BlockchainInfoResponse>> getWeb3Info(){


        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>( web3Service.getBlockchainInfo(),"Blockchain info",true)
        );
    }

    @GetMapping("/private/batch/{id}")
    public ResponseEntity<CustomApiResponse<HashBatchInfoDTO>> getBatchInfo(@PathVariable  String id){
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(web3Service.getBatchInfo(id),"Batch info",true)
        );
    }

    @GetMapping("/public/get-balance")
    public ResponseEntity<CustomApiResponse<String>> getBalance(){
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(web3Service.getBalance(),"Account balance in Ethers (Avalanche)",true)
        );
    }





    @GetMapping("/private/current-batch")
    public ResponseEntity<CustomApiResponse<HashBatchInfoDTO>> getCurrentBatchInfo(){

        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(web3Service.getCurrentBatchInfo(),"Current batch info",true)
        );
    }


    @GetMapping("/private/contract-state")
    public ResponseEntity<CustomApiResponse<ContractStateDTO>> getContractState(){

        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(     web3Service.getContractState(),"Contract state",true)
        );
    }


}
