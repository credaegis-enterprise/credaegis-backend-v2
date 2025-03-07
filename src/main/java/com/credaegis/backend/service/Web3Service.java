package com.credaegis.backend.service;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

    @Value("${credaegis.web3.contract-address}")
    private String contractAddress;


    public String getBalance() {
        try {
            EthGetBalance ethGetBalance = web3j.ethGetBalance(
                    contractAddress,
                    DefaultBlockParameterName.LATEST
            ).send();

            BigDecimal balanceInWei = new BigDecimal(ethGetBalance.getBalance());
            BigDecimal balanceInEther = Convert.fromWei(balanceInWei, Convert.Unit.ETHER);
            log.info("Balance in Ether: {}", balanceInEther);
            return balanceInEther.toPlainString();
        } catch (Exception e) {
            log.error("Error fetching balance: {}", e.getMessage());
            return null;
        }
    }
}