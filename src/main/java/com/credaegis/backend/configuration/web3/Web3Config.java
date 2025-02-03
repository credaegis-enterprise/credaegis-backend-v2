package com.credaegis.backend.configuration.web3;


import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
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

    @Value("${credaegis.web3.chain-id}")
    private Long chainId;


    //Loading the contract deployed on the public blockchain

    @Bean
    HashStore hashStore(Web3j web3j){

        Credentials credentials = Credentials.create(privateKey);
        ContractGasProvider gasProvider = new StaticGasProvider(
                Convert.toWei("5", Convert.Unit.GWEI).toBigInteger(),
                BigInteger.valueOf(5_000_000)
        );

        RawTransactionManager transactionManager = new RawTransactionManager(
                web3j, credentials, chainId);


        HashStore contract = HashStore.load(contractAddress,web3j,transactionManager,gasProvider);
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

//    @PreDestroy
//    public void shutdownWeb3j(Web3j web3j) {
//        if (web3j != null) {
//            try {
//                web3j.shutdown();
//                log.info("Web3j client shut down successfully.");
//            } catch (Exception e) {
//                log.error("Error shutting down Web3j client: {}", e.getMessage(), e);
//            }
//        }
//    }
}
