package com.credaegis.backend.service.organization;


import com.credaegis.backend.dto.CertificateVerificationBlockchainResultDTO;
import com.credaegis.backend.entity.Certificate;
import com.credaegis.backend.exception.custom.CustomException;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.http.response.custom.CertificateVerificationResponse;
import com.credaegis.backend.dto.CertificateVerificationInfoDTO;
import com.credaegis.backend.repository.CertificateRepository;
import com.credaegis.backend.utility.CheckSumUtility;
import com.credaegis.backend.utility.HttpUtility;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VerificationService {


    private final CertificateRepository certificateRepository;
    private final CheckSumUtility checkSumUtility;
    private final RestTemplate restTemplate;
    private final Web3Service web3Service;


    @Value("${credaegis.async-blockchain.service.url}")
    private String asyncEndPoint;

    @Value("${credaegis.async-blockchain.service.api-key}")
    private String apiKey;


    public List<CertificateVerificationResponse> verifyAuthenticityBlockchain(List<MultipartFile> certificateFiles) throws IOException {
        List<String> hashes = new ArrayList<>();
        Map<String, String> nameHashMap = new HashMap<>();
        Map<String,Boolean> merkleRootVerifiedHash = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        for (MultipartFile file : certificateFiles) {
            String hash = checkSumUtility.hashCertificate(file.getBytes());
            if (nameHashMap.containsKey(hash))
                throw ExceptionFactory.customValidationError("Duplicate files found" + " " + file.getOriginalFilename() +
                        " and " + nameHashMap.get(hash));

            nameHashMap.put(hash, file.getOriginalFilename());
            hashes.add(hash);
        }


        ResponseEntity<String> response;
        HttpEntity<Object> requestEntity = new HttpEntity<>(hashes, HttpUtility.getApiKeyHeader(apiKey));

        try {
            response = restTemplate.postForEntity(asyncEndPoint + "" +
                    "/verify", requestEntity, String.class);

        } catch (Exception e) {
            throw new CustomException("Blockchain verification service is down", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<CertificateVerificationBlockchainResultDTO> verificationResult = objectMapper.readValue(response.getBody(),
                new TypeReference<List<CertificateVerificationBlockchainResultDTO>>() {
                });
        List<String> merkleRoots = verificationResult.stream()
                .filter(result -> result.getIsVerified() && result.getMerkleRoot() != null)
                .map(CertificateVerificationBlockchainResultDTO::getMerkleRoot)
                .collect(Collectors.toList());

        log.info("MerkleRoots: " + merkleRoots);

        if(!merkleRoots.isEmpty())
            merkleRootVerifiedHash = web3Service.verifyMerkleRootPublic(merkleRoots);

        List<CertificateVerificationResponse> certificateVerificationResponseList = new ArrayList<>();
        for(CertificateVerificationBlockchainResultDTO result:verificationResult){
            Optional<Certificate> optionalCertificate = certificateRepository.findByCertificateHash(result.getHash());
            CertificateVerificationResponse certificateVerificationResponse = new CertificateVerificationResponse();
            certificateVerificationResponse.setCertificateName(nameHashMap.get(result.getHash()));
            if(!result.getIsVerified()) {
                certificateVerificationResponse.setIsIssued(false);
                certificateVerificationResponse.setIsPublicVerified(false);
            }
            else {
                certificateVerificationResponse.setIsIssued(true);
                if(merkleRootVerifiedHash.containsKey(result.getMerkleRoot()))
                    certificateVerificationResponse.setIsPublicVerified(merkleRootVerifiedHash.get(result.getMerkleRoot()));
                else
                    certificateVerificationResponse.setIsPublicVerified(false);
            }

            if(optionalCertificate.isEmpty()){
                certificateVerificationResponse.setInfoFound(false);
                certificateVerificationResponse.setCertificateVerificationInfoDTO(null);
                certificateVerificationResponseList.add(certificateVerificationResponse);
                continue;
            }

            Certificate certificate = optionalCertificate.get();
            certificateVerificationResponse.setInfoFound(true);
            CertificateVerificationInfoDTO info = CertificateVerificationInfoDTO.builder()
                    .certificateName(nameHashMap.get(result.getHash()))
                    .certificateId(certificate.getId())
                    .recipientName(certificate.getRecipientName())
                    .recipientEmail(certificate.getRecipientEmail())
                    .clusterName(certificate.getEvent().getCluster().getName())
                    .organizationName(certificate.getEvent().getCluster().getOrganization().getName())
                    .revoked(certificate.getRevoked())
                    .issuedDate(certificate.getIssuedDate())
                    .comments(certificate.getComments())
                    .expiryDate(certificate.getExpiryDate())
                    .clusterName(certificate.getEvent().getCluster().getName())
                    .eventName(certificate.getEvent().getName())
                    .build();



            certificateVerificationResponse.setCertificateVerificationInfoDTO(info);
            certificateVerificationResponseList.add(certificateVerificationResponse);
        }

        return certificateVerificationResponseList;

    }


    //This service is used for verification by blockchain (private only)
//    public List<CertificateVerificationResponse> verifyAuthenticityBlockchain(List<MultipartFile> certificateFiles) throws IOException {
//        List<String> hashes = new ArrayList<>();
//        Map<String,String> nameHashMap = new HashMap<>();
//        ObjectMapper objectMapper = new ObjectMapper();
//        for (MultipartFile file : certificateFiles) {
//            String hash = checkSumUtility.hashCertificate(file.getBytes());
//            if(nameHashMap.containsKey(hash))
//                throw ExceptionFactory.customValidationError("Duplicate files found" + " " + file.getOriginalFilename() +
//                        " and " + nameHashMap.get(hash));
//
//            nameHashMap.put(hash, file.getOriginalFilename());
//            hashes.add(hash);
//        }
//
//
//        ResponseEntity<String> response;
//
//
//        HttpEntity<Object> requestEntity = new HttpEntity<>(hashes, HttpUtility.getApiKeyHeader(apiKey));
//
//        try{
//        response = restTemplate.postForEntity(asyncEndPoint+"" +
//                "/verify",requestEntity, String.class);
//        }catch (Exception e){
//            throw new CustomException("Blockchain verification service is down", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//
//        System.out.println(response.getBody());
//        List<CertificateVerificationBlockchainResultDTO> verificationResult = objectMapper.readValue(response.getBody(),
//                new TypeReference<List<CertificateVerificationBlockchainResultDTO>>() {
//                });
//
//        List<CertificateVerificationResponse> certificateVerificationResponseList = new ArrayList<>();
//        for(CertificateVerificationBlockchainResultDTO result:verificationResult){
//            Optional<Certificate> optionalCertificate = certificateRepository.findByCertificateHash(result.getHash());
//            CertificateVerificationResponse certificateVerificationResponse = new CertificateVerificationResponse();
//            certificateVerificationResponse.setCertificateName(nameHashMap.get(result.getHash()));
//            if(!result.getIsVerified())
//                certificateVerificationResponse.setIsIssued(false);
//            else
//                certificateVerificationResponse.setIsIssued(true);
//
//            if(optionalCertificate.isEmpty()){
//                certificateVerificationResponse.setInfoFound(false);
//                certificateVerificationResponse.setCertificateVerificationInfoDTO(null);
//                certificateVerificationResponseList.add(certificateVerificationResponse);
//                continue;
//            }
//
//            Certificate certificate = optionalCertificate.get();
//            certificateVerificationResponse.setInfoFound(true);
//            CertificateVerificationInfoDTO info = CertificateVerificationInfoDTO.builder()
//                    .certificateName(nameHashMap.get(result.getHash()))
//                    .certificateId(certificate.getId())
//                    .recipientName(certificate.getRecipientName())
//                    .recipientEmail(certificate.getRecipientEmail())
//                    .clusterName(certificate.getEvent().getCluster().getName())
//                    .organizationName(certificate.getEvent().getCluster().getOrganization().getName())
//                    .revoked(certificate.getRevoked())
//                    .issuedDate(certificate.getIssuedDate())
//                    .comments(certificate.getComments())
//                    .expiryDate(certificate.getExpiryDate())
//                    .clusterName(certificate.getEvent().getCluster().getName())
//                    .eventName(certificate.getEvent().getName())
//                    .build();
//
//
//
//            certificateVerificationResponse.setCertificateVerificationInfoDTO(info);
//            certificateVerificationResponseList.add(certificateVerificationResponse);
//        }
//
//        return certificateVerificationResponseList;
//
//    }


    //off-chain solution
    public List<CertificateVerificationResponse> verifyAuthenticity(List<MultipartFile> certificateFiles) throws IOException {

        List<CertificateVerificationResponse> certificateVerificationResponseList = new ArrayList<>();
        for (MultipartFile file : certificateFiles) {
            System.out.println("innnn");
            CertificateVerificationResponse certificateVerificationResponse = new CertificateVerificationResponse();
            Optional<Certificate> optionalCertificate = certificateRepository.findByCertificateHash(checkSumUtility.hashCertificate(file.getBytes()));
            certificateVerificationResponse.setCertificateName(file.getOriginalFilename());
            if (!optionalCertificate.isPresent()) {
                certificateVerificationResponse.setIsIssued(false);
                certificateVerificationResponse.setCertificateVerificationInfoDTO(null);
                certificateVerificationResponseList.add(certificateVerificationResponse);
                continue;
            }

            Certificate certificate = optionalCertificate.get();
            certificateVerificationResponse.setIsIssued(true);

            CertificateVerificationInfoDTO info = CertificateVerificationInfoDTO.builder()
                    .certificateName(file.getOriginalFilename())
                    .certificateId(certificate.getId())
                    .recipientName(certificate.getRecipientName())
                    .recipientEmail(certificate.getRecipientEmail())
                    .clusterName(certificate.getEvent().getCluster().getName())
                    .organizationName(certificate.getEvent().getCluster().getOrganization().getName())
                    .revoked(certificate.getRevoked())
                    .issuedDate(certificate.getIssuedDate())
                    .comments(certificate.getComments())
                    .expiryDate(certificate.getExpiryDate())
                    .clusterName(certificate.getEvent().getCluster().getName())
                    .eventName(certificate.getEvent().getName())
                    .build();

            certificateVerificationResponse.setCertificateVerificationInfoDTO(info);
            certificateVerificationResponseList.add(certificateVerificationResponse);

        }

        return certificateVerificationResponseList;

    }

}
