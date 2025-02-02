package com.credaegis.backend.configuration.web3;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;

import java.math.BigInteger;

@Configuration
@Slf4j
public class Web3Config {

    @Value("${credaegis.web3.rpc-url}")
    private String rpcUrl;


    @Value("${credaegis.web3.contract-address}")
    private String contractAddress;


    @Value("${credaegis.web3.private-key}")
    private String privateKey;


    //Loading the contract deploayed on the public blockchain

    @Bean
    HashStore hashStore(Web3j web3j){

        Credentials credentials = Credentials.create(privateKey);
        ContractGasProvider gasProvider = new StaticGasProvider(
                Convert.toWei("25", Convert.Unit.GWEI).toBigInteger(),
                BigInteger.valueOf(5_000_000)
        );

        HashStore contract = HashStore.load(contractAddress,web3j,credentials,gasProvider);
        log.info("Contract loaded: " + contract.getContractAddress());
        return contract;

    }

    @Bean
    Web3j connect(){
        try{
            Web3j web3j = Web3j.build(new HttpService(rpcUrl));
            Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
            log.info("Connected to Ethereum client version: " + web3ClientVersion.getWeb3ClientVersion());
            return web3j;
        }
        catch (Exception e){
            log.error("Error connecting to Ethereum client: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error connecting to Ethereum client");
        }

    }
}
