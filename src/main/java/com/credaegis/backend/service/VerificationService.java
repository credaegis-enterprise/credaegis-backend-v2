package com.credaegis.backend.service;


import com.credaegis.backend.entity.Certificate;
import com.credaegis.backend.http.response.custom.CertificateVerificationResponse;
import com.credaegis.backend.dto.CertificateVerificationInfoDTO;
import com.credaegis.backend.repository.CertificateRepository;
import com.credaegis.backend.utility.CheckSumUtility;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class VerificationService {

    private final CertificateRepository certificateRepository;
    private final CheckSumUtility checkSumUtility;

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
                    .build();

            certificateVerificationResponse.setCertificateVerificationInfoDTO(info);
            certificateVerificationResponseList.add(certificateVerificationResponse);

        }

        return certificateVerificationResponseList;

    }

}
