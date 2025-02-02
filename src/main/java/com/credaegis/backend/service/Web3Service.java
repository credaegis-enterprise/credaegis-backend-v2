package com.credaegis.backend.service;


import com.credaegis.backend.configuration.web3.HashStore;
import com.credaegis.backend.dto.ContractStateDTO;
import com.credaegis.backend.dto.HashBatchInfoDTO;
import com.credaegis.backend.dto.Web3InfoDTO;
import com.credaegis.backend.exception.custom.CustomException;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.http.response.custom.BlockchainInfoResponse;
import com.credaegis.backend.utility.MerkleTreeUtility;
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
import org.web3j.protocol.core.methods.response.NetVersion;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
@Service
@Slf4j
public class Web3Service {

    private final Web3j web3j;
    private final ObjectMapper objectMapper;
    private final HashStore hashStore;

    @Value("${credaegis.web3.contract-address}")
    private String contractAddress;

    @Value("${credaegis.async-blockchain.service.url}")
    private String asyncEndPoint;

    private final RestTemplate restTemplate;


    public void storeCurrentBatchMerkleRootToPublic() {
//        String merkleRoot = getCurrentBatchMerkleRoot();
        try {
            TransactionReceipt transactionReceipt = hashStore.storeHash(new ArrayList<>(List.of("abj"))).send();
            String transc = objectMapper.writeValueAsString(transactionReceipt);
            log.info("Transaction receipt: {}", transc);
        } catch (Exception e) {
            log.error("Error storing merkle root to public blockchain: {}", e.getMessage());
            throw new CustomException("Error storing merkle root to public blockchain", HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }


    public String getCurrentBatchMerkleRoot() {
        HashBatchInfoDTO hashBatchInfoDTO = getCurrentBatchInfo();
        List<String> hashes = hashBatchInfoDTO.getHashes();
        if (hashes.size() == 0) {
            throw ExceptionFactory.customValidationError("No hashes found in the current batch");
        }
        return MerkleTreeUtility.calculateMerkleRoot(hashes);
    }


    //public info
    public BlockchainInfoResponse getBlockchainInfo() {

        try {

            Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();

            NetVersion netVersion = web3j.netVersion().send();
            String networkId = netVersion.getNetVersion();

            Web3InfoDTO web3InfoDTO = Web3InfoDTO
                    .builder()
                    .networkId(networkId)
                    .clientVersion(clientVersion)
                    .networkName("Avalanche")
                    .build();


            ContractStateDTO contractStateDTO = getContractState();

            HashBatchInfoDTO hashBatchInfoDTO = getCurrentBatchInfo();
            hashBatchInfoDTO.setBatchId(contractStateDTO.getCurrentBatchIndex());
            BlockchainInfoResponse blockchainInfoResponse =
                    BlockchainInfoResponse.builder()
                            .hashBatchInfoDTO(hashBatchInfoDTO)
                            .web3InfoDTO(web3InfoDTO)
                            .build();

            return blockchainInfoResponse;
        } catch (Exception e) {
            log.error("Error fetching blockchain information: {}", e.getMessage());
            throw new CustomException("Blockchain verification service is down", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public HashBatchInfoDTO getBatchInfo(String id) {
        System.out.println("id: " + id);
        ResponseEntity<String> response;
        Map<String, String> idHashMap = new HashMap<>();
        idHashMap.put("id", id);

        ContractStateDTO contractStateDTO = getContractState();
        if (Integer.parseInt(id) > Integer.parseInt(contractStateDTO.getCurrentBatchIndex()) || Integer.parseInt(id) < 1) {
            throw new CustomException("Batch not found, the current batch index is: " + contractStateDTO.getCurrentBatchIndex(), HttpStatus.NOT_FOUND);
        }
        try {
            response = restTemplate.getForEntity(asyncEndPoint + "/batch/{id}", String.class, idHashMap);
            log.info("Batch info: {}", response.getBody());
            HashBatchInfoDTO hashBatchInfoDTO = objectMapper.readValue(response.getBody(), HashBatchInfoDTO.class);
            return hashBatchInfoDTO;
        } catch (Exception e) {
            throw new CustomException("Blockchain verification service is down", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public HashBatchInfoDTO getCurrentBatchInfo() {
        ResponseEntity<String> response;
        try {
            response = restTemplate.getForEntity(asyncEndPoint + "/current-batch", String.class);
            log.info("Current batch info: {}", response.getBody());
            HashBatchInfoDTO hashBatchInfoDTO = objectMapper.readValue(response.getBody(), HashBatchInfoDTO.class);
            return hashBatchInfoDTO;
        } catch (Exception e) {
            throw new CustomException("Blockchain verification service is down", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ContractStateDTO getContractState() {

        ResponseEntity<String> response;


        try {
            response = restTemplate.getForEntity(asyncEndPoint + "/contract-state", String.class);
            log.info("Contract state: {}", response.getBody());
            ContractStateDTO contractStateDTO = objectMapper.readValue(response.getBody(), ContractStateDTO.class);
            return contractStateDTO;
        } catch (Exception e) {
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
            log.info("Balance in AVAX: {}", balanceInEther);
            return balanceInEther.toPlainString();
        } catch (Exception e) {
            log.error("Error fetching balance: {}", e.getMessage());
            return null;
        }
    }
}