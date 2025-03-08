package com.credaegis.backend.service;


import com.credaegis.backend.configuration.web3.HashStore;
import com.credaegis.backend.dto.ContractStateDTO;
import com.credaegis.backend.dto.FinalizeBatchDTO;
import com.credaegis.backend.dto.HashBatchInfoDTO;
import com.credaegis.backend.dto.Web3InfoDTO;
import com.credaegis.backend.entity.BatchInfo;
import com.credaegis.backend.entity.Certificate;
import com.credaegis.backend.exception.custom.CustomException;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.http.response.custom.BlockchainInfoResponse;
import com.credaegis.backend.repository.BatchInfoRepository;
import com.credaegis.backend.repository.CertificateRepository;
import com.credaegis.backend.utility.MerkleTreeUtility;
import com.credaegis.backend.utility.Web3Utility;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

@Data
@RequiredArgsConstructor
@Service
@Slf4j
public class Web3Service {

    private final Web3j web3j;
    private final ObjectMapper objectMapper;
    private final HashStore hashStore;
    private final BatchInfoRepository batchInfoRepository;
    private final CertificateRepository certificateRepository;
    private final RestTemplate restTemplate;

    @Value("${credaegis.web3.contract-address}")
    private String contractAddress;


    @Value("${credaegis.web3.wallet-address}")
    private String walletAddress;

    @Value("${credaegis.async-blockchain.service.url}")
    private String asyncEndPoint;

    @Value("${credaegis.web3.chain-id}")
    private String chainId;

    @Value("${credaegis.web3.txn.url}")
    private String snowTraceTxnUrl;




    public String getTxnDetails(String hash){
        try {
            System.out.println("hash: " + hash);
            TransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt(hash).send().getTransactionReceipt().get();
            return snowTraceTxnUrl+hash+"?"+"chainid="+chainId;
        } catch (Exception e) {
            log.error("Error fetching transaction receipt: {}", e.getMessage());
            throw new CustomException("Error fetching transaction receipt", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    public Map<String,Boolean> verifyMerkleRootPublic(List<String> merkleRoots) {
        try {
            Map<String, Boolean> verificationHashMap = new HashMap<>();
            List<HashStore.VerificationResult> result = hashStore.verifyHashesByValue(merkleRoots).send();

            for (HashStore.VerificationResult item : result) {
                verificationHashMap.put(item.verifiedHashes, item.verificationResults);
            }

            return verificationHashMap;
        } catch (Exception e) {
            log.error("Error verifying merkle root from public blockchain: {}", e.getMessage());
            throw new CustomException("Error verifying merkle root from public blockchain", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public String finalizeBatch(){
        String merkleRoot = getCurrentBatchMerkleRoot();
        log.info("Merkle root for current batch calculated successfully: {}", merkleRoot);
        FinalizeBatchDTO finalizeBatchDTO;
        ResponseEntity<String> response;
        try{

            Map<String, String> merkleRootMap = new HashMap<>();
            merkleRootMap.put("merkleRoot", merkleRoot);

            response = restTemplate.postForEntity(asyncEndPoint + "/finalize/{merkleRoot}",null, String.class, merkleRootMap);
            finalizeBatchDTO = objectMapper.readValue(response.getBody(), FinalizeBatchDTO.class);
            log.info("Batch finalized successfully: {}", finalizeBatchDTO);
            return merkleRoot;
        }
        catch (Exception e) {
            log.error("Error in finalizing Batch: {}", e.getMessage());
            throw new CustomException("Error in finalizing batch", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @Async
    public void storeCurrentBatchMerkleRootToPublic() {

        String merkleRoot = finalizeBatch();
        HashBatchInfoDTO hashBatchInfoDTO = getCurrentBatchInfo();
        try {

            TransactionReceipt transactionReceipt = hashStore.storeHash(new ArrayList<>(List.of(merkleRoot))).send();
            String transc = objectMapper.writeValueAsString(transactionReceipt);
            log.info("Transaction receipt: {}", transc);
            BatchInfo batchInfo = new BatchInfo();
            batchInfo.setId(parseInt(hashBatchInfoDTO.getBatchId()));
            batchInfo.setMerkleRoot(merkleRoot);
            batchInfo.setPushTime(new java.sql.Timestamp(System.currentTimeMillis()));
            batchInfo.setHashCount(hashBatchInfoDTO.getHashes().size());
            batchInfo.setTxnHash(transactionReceipt.getTransactionHash());
            batchInfo.setTxnFee(Web3Utility.covertToAVAX(transactionReceipt).toPlainString());
            batchInfoRepository.save(batchInfo);


        } catch (Exception e) {
            log.error("Error storing merkle root to public blockchain: {}", e.getMessage());
            throw new CustomException("Error storing merkle root to public blockchain", HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }


    public String getCurrentBatchMerkleRoot() {
        HashBatchInfoDTO hashBatchInfoDTO = getCurrentBatchInfo();
        List<String> hashes = hashBatchInfoDTO.getHashes();
        log.info("Hashes for merkle root calc: {}", hashes);
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
                    .balance(getBalance())
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
        if (parseInt(id) > parseInt(contractStateDTO.getCurrentBatchIndex()) || parseInt(id) < 1) {
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
            hashBatchInfoDTO.setBatchId(getContractState().getCurrentBatchIndex());
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
                    walletAddress,
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