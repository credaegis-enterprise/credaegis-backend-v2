package com.credaegis.backend.service.organization;


import com.credaegis.backend.configuration.web3.HashStore;
import com.credaegis.backend.dto.*;
import com.credaegis.backend.entity.BatchInfo;
import com.credaegis.backend.entity.Certificate;
import com.credaegis.backend.entity.CertificateStatus;
import com.credaegis.backend.exception.custom.CustomException;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.http.response.custom.BlockchainInfoResponse;
import com.credaegis.backend.repository.BatchInfoRepository;
import com.credaegis.backend.repository.CertificateRepository;
import com.credaegis.backend.utility.HttpUtility;
import com.credaegis.backend.utility.MerkleTreeUtility;
import com.credaegis.backend.utility.Web3Utility;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static java.lang.Integer.parseInt;


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

    @Value("${credaegis.async-blockchain.service.api-key}")
    private String apiKey;


    public String getTxnDetails(String hash) {
        try {
            System.out.println("hash: " + hash);
            TransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt(hash).send().getTransactionReceipt().get();
            return snowTraceTxnUrl + hash + "?" + "chainid=" + chainId;
        } catch (Exception e) {
            log.error("Error fetching transaction receipt: {}", e.getMessage());
            throw new CustomException("Error fetching transaction receipt", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    public Map<String,String> getMerkleRootByHashes(List<String> hashes)
    {
        Map<String,String> hashMerkleRootMap = new HashMap<>();
        ResponseEntity<Object> response;
        HttpEntity<Object> requestEntity = new HttpEntity<>(hashes,HttpUtility.getApiKeyHeader(apiKey));
        try {
            response = restTemplate.postForEntity(asyncEndPoint + "/hashes/merkle-root", requestEntity, Object.class);
            List<HashMerkleRootDTO> hashMerkleRootDTOList = objectMapper.convertValue(
                    response.getBody(), new TypeReference<List<HashMerkleRootDTO>>() {}
            );

            hashMerkleRootDTOList.forEach(hashMerkleRootDTO -> {
                hashMerkleRootMap.put(hashMerkleRootDTO.getHash(), hashMerkleRootDTO.getMerkleRoot());
            });
            return hashMerkleRootMap;
        } catch (Exception e) {
            log.error("Error fetching merkle root from public blockchain: {}", e.getMessage());
            throw new CustomException("Error fetching merkle root from public blockchain", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public Map<String, Boolean> verifyMerkleRootPublic(List<String> merkleRoots) {
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


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String finalizeBatch() {
        String merkleRoot = getCurrentBatchMerkleRoot();
        log.info("Merkle root for current batch calculated successfully: {}", merkleRoot);
        FinalizeBatchDTO finalizeBatchDTO;
        ResponseEntity<String> response;
        try {

            Map<String, String> merkleRootMap = new HashMap<>();
            merkleRootMap.put("merkleRoot", merkleRoot);
            HttpEntity<Object> requestEntity = new HttpEntity<>(HttpUtility.getApiKeyHeader(apiKey));

            try {
                response = restTemplate.
                        postForEntity(asyncEndPoint + "/finalize/{merkleRoot}", requestEntity,
                                String.class, merkleRootMap);
            }
            catch (Exception e){
                throw new CustomException("Error in finalizing batch in private chain", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            finalizeBatchDTO = objectMapper.readValue(response.getBody(), FinalizeBatchDTO.class);
            log.info("Batch finalized successfully in private chain: {}", finalizeBatchDTO);
            BatchInfo batchInfo = new BatchInfo();
            batchInfo.setId(parseInt(finalizeBatchDTO.getBatchId()));
            batchInfo.setMerkleRoot(merkleRoot);
            log.info("finilized finish");
            batchInfoRepository.save(batchInfo);
            return merkleRoot;
        } catch (Exception e) {
            log.error("Error in finalizing Batch in private chain: {}", e.getMessage());
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void storeCurrentBatchMerkleRootToPublic()  {

        //not will be in same transaction (invocation from same class) this is the required behaviour
        HashBatchInfoDTO hashBatchInfoDTO = getCurrentBatchInfo();

        //all hash batch info changes after this since batch is finalized
        String merkleRoot = finalizeBatch();
        log.info("finalized batch method finished..........");
        BatchInfo batchInfo = batchInfoRepository.findOneById(parseInt(hashBatchInfoDTO.getBatchId())).orElseThrow(
                () -> new CustomException("Batch not found", HttpStatus.NOT_FOUND)
        );
        TransactionReceipt transactionReceipt;
        try {

            batchInfo.setId(parseInt(hashBatchInfoDTO.getBatchId()));
            batchInfo.setPushTime(new java.sql.Timestamp(System.currentTimeMillis()));
            batchInfo.setHashCount(hashBatchInfoDTO.getHashes().size());
            try {

                transactionReceipt = hashStore.storeHash(new ArrayList<>(List.of(merkleRoot))).send();
                log.info("Successfully stored to public chain");
                batchInfo.setTxnFee(Web3Utility.covertToAVAX(transactionReceipt).toPlainString());
                batchInfo.setTxnHash(transactionReceipt.getTransactionHash());
                batchInfo.setPushStatus(true);
            } catch (Exception e) {
                log.error("Error occured in storing to public chain, {}", e.getMessage());
                batchInfo.setPushStatus(false);
            }


            batchInfoRepository.save(batchInfo);
            log.info("Batch info hashes :{}", hashBatchInfoDTO.getHashes());
            certificateRepository.updateBatchInfo(parseInt(hashBatchInfoDTO.getBatchId()),
                    hashBatchInfoDTO.getHashes(), CertificateStatus.publicVerified);


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
        ResponseEntity<String> response;
        Map<String, String> idHashMap = new HashMap<>();
        idHashMap.put("id", id);

        ContractStateDTO contractStateDTO = getContractState();
        if (parseInt(id) > parseInt(contractStateDTO.getCurrentBatchIndex()) || parseInt(id) < 1) {
            throw new CustomException("Batch not found, the current batch index is: " + contractStateDTO.getCurrentBatchIndex(), HttpStatus.NOT_FOUND);
        }
        try {

            HttpEntity<Object> requestEntity = new HttpEntity<>(HttpUtility.getApiKeyHeader(apiKey));
//            response = restTemplate.getForEntity(asyncEndPoint + "/batch/{id}", String.class, idHashMap);
            String url = asyncEndPoint + "/batch/{id}";
            try{
            response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class, idHashMap);
            }
            catch (Exception e){
                log.error("Error : {}", e.getMessage());
                throw new CustomException("Blockchain service is down", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            log.info("Batch info: {}", response.getBody());
            HashBatchInfoDTO hashBatchInfoDTO = objectMapper.readValue(response.getBody(), HashBatchInfoDTO.class);
            hashBatchInfoDTO.setBatchId(id);
            Optional<BatchInfo> batchInfo = batchInfoRepository.findOneById(parseInt(id));
            if(batchInfo.isPresent()){
                hashBatchInfoDTO.setBatchInfo(batchInfo.get());
            }
            return hashBatchInfoDTO;

        } catch (Exception e) {
            log.error("Error : {}", e.getMessage());
            throw new CustomException("Error getting batch info", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public HashBatchInfoDTO getCurrentBatchInfo() {
        ResponseEntity<String> response;
        try {

            HttpEntity<Object> requestEntity = new HttpEntity<>(HttpUtility.getApiKeyHeader(apiKey));
            response = restTemplate.exchange(asyncEndPoint + "/current-batch", HttpMethod.GET, requestEntity, String.class);
            log.info("Current batch info: {}", response.getBody());
            HashBatchInfoDTO hashBatchInfoDTO = objectMapper.readValue(response.getBody(), HashBatchInfoDTO.class);
            hashBatchInfoDTO.setBatchId(getContractState().getCurrentBatchIndex());
            Optional<BatchInfo> batchInfo = batchInfoRepository.findOneById(parseInt(hashBatchInfoDTO.getBatchId()));
            if(batchInfo.isPresent()){
                hashBatchInfoDTO.setBatchInfo(batchInfo.get());
            }

            return hashBatchInfoDTO;
        } catch (Exception e) {
            throw new CustomException("Blockchain verification service is down", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ContractStateDTO getContractState() {

        ResponseEntity<String> response;


        try {

            HttpEntity<Object> requestEntity = new HttpEntity<>(HttpUtility.getApiKeyHeader(apiKey));
            response = restTemplate.exchange(asyncEndPoint + "/contract-state", HttpMethod.GET, requestEntity, String.class);

//            response = restTemplate.getForEntity(asyncEndPoint + "/contract-state", String.class);
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