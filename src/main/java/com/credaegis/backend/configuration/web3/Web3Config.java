package com.credaegis.backend.configuration.web3;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

@Configuration
@Slf4j
public class Web3Config {

    @Value("${credaegis.web3.rpc-url}")
    private String rpcUrl;





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
