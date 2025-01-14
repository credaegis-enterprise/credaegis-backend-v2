package com.credaegis.backend.service;


import com.credaegis.backend.dto.ContractStateDTO;
import com.credaegis.backend.exception.custom.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
@Service
@Slf4j
public class Web3Service {

    private final Web3j web3j;
    private final ObjectMapper objectMapper;

    @Value("${credaegis.web3.contract-address}")
    private String contractAddress;

    @Value("${credaegis.async-blockchain.service.url}")
    private String asyncEndPoint;


    private final RestTemplate restTemplate;



    public ContractStateDTO getContractState() {

        ResponseEntity<String> response;

        try{
            response = restTemplate.getForEntity(asyncEndPoint+"/contract-state", String.class);
            log.info("Contract state: {}", response.getBody());
            ContractStateDTO contractStateDTO = objectMapper.readValue(response.getBody(), ContractStateDTO.class);
            return contractStateDTO;
        }catch (Exception e){
            throw new CustomException("Blockchain verification service is down", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public String getBalance() {
        try {
            EthGetBalance ethGetBalance = web3j.ethGetBalance(
                    contractAddress,
                    DefaultBlockParameterName.LATEST
            ).send();

            BigDecimal balanceInWei = new BigDecimal(ethGetBalance.getBalance());
            BigDecimal balanceInEther = Convert.fromWei(balanceInWei, Convert.Unit.ETHER);
            log.info("Balance in Ether (Avalanche): {}", balanceInEther);
            return balanceInEther.toPlainString();
        } catch (Exception e) {
            log.error("Error fetching balance: {}", e.getMessage());
            return null;
        }
    }
}