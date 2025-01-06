package com.credaegis.backend.service;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;

@Data
@RequiredArgsConstructor
@Service
@Slf4j
public class BlockchainService {

    private final Web3j web3j;

    @Value("${credaegis.web3.contract-address}")
    private String contractAddress;


    public Long getBalance() {

        try {
            EthGetBalance ethGetBalance = web3j.ethGetBalance(
                    contractAddress,
                    DefaultBlockParameterName.LATEST
            ).send();
            return ethGetBalance.getBalance().longValue();
        } catch (Exception e) {

            log.error("Error fetching balance: {}", e.getMessage());
            return null;
        }
    }


}
