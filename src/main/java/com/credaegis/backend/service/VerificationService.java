package com.credaegis.backend.service;


import com.credaegis.backend.dto.CertificateVerificationBlockchainResultDTO;
import com.credaegis.backend.entity.Certificate;
import com.credaegis.backend.http.response.custom.CertificateVerificationResponse;
import com.credaegis.backend.dto.CertificateVerificationInfoDTO;
import com.credaegis.backend.repository.CertificateRepository;
import com.credaegis.backend.utility.CheckSumUtility;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class VerificationService {


    private final CertificateRepository certificateRepository;
    private final CheckSumUtility checkSumUtility;
    private final RestTemplate restTemplate;


    @Value("${credaegis.blockchain.url}")
    private String asyncEndPoint;

    public VerificationService(CertificateRepository certificateRepository,CheckSumUtility checkSumUtility,
                               RestTemplate restTemplate){

        this.restTemplate = restTemplate;
        this.certificateRepository = certificateRepository;
        this.checkSumUtility = checkSumUtility;
    }


    //This service is used for verification by blockchain
    public void verifyAuthenticityBlockchain(List<MultipartFile> certificateFiles) throws IOException {
        List<String> hashes = new ArrayList<>();
        Map<String,String> nameHashMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        for (MultipartFile file : certificateFiles) {
            String hash = checkSumUtility.hashCertificate(file.getBytes());
            nameHashMap.put(hash, file.getOriginalFilename());
            hashes.add(hash);
        }

        ResponseEntity<String> response = restTemplate.postForEntity(asyncEndPoint+"" +
                "/verify",hashes, String.class);
        List<CertificateVerificationBlockchainResultDTO> verificationResult = objectMapper.readValue(response.getBody(),
                new TypeReference<List<CertificateVerificationBlockchainResultDTO>>() {
                });

        List<CertificateVerificationResponse> certificateVerificationResponseList = new ArrayList<>();
        for(CertificateVerificationBlockchainResultDTO result:verificationResult){
            Optional<Certificate> optionalCertificate = certificateRepository.findByCertificateHash(result.getHash());
            CertificateVerificationResponse certificateVerificationResponse = new CertificateVerificationResponse();
            if(!result.getIsVerified()){
                if(!optionalCertificate.isPresent()){
                    certificateVerificationResponse.
                }
            }

        }

    }


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
