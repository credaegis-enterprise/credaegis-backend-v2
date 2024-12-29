package com.credaegis.backend.service;


import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.dto.CertificateInfoDTO;
import com.credaegis.backend.dto.RevocationBlockchainDTO;
import com.credaegis.backend.dto.projection.CertificateInfoProjection;
import com.credaegis.backend.entity.Certificate;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.repository.CertificateRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final RabbitTemplate rabbitTemplate;


    public void revokeCertificatesBlockchain(List<String> certificateIds, String userOrganizationId,String userId) {

        for (String certificateId : certificateIds) {

            try {
                Certificate certificate = certificateRepository.findById(certificateId).orElseThrow(ExceptionFactory::resourceNotFound);
                if (!certificate.getEvent().getCluster().getOrganization().getId().equals(userOrganizationId))
                    throw ExceptionFactory.insufficientPermission();

                RevocationBlockchainDTO revocationBlockchainDTO = new RevocationBlockchainDTO();
                revocationBlockchainDTO.setRevokerId(userId);
                revocationBlockchainDTO.setCertificateId(certificateId);
                revocationBlockchainDTO.setHash(certificate.getCertificateHash());

                rabbitTemplate.convertAndSend(Constants.DIRECT_EXCHANGE,Constants.CERTIFICATE_REVOKE_REQUEST_QUEUE_KEY,revocationBlockchainDTO);

            } catch (Exception e) {

                //dead letter queue
                log.error(e.getMessage());
                log.error("error processing certificate id {}", certificateId);
                String errorMessage = "certificate id " + certificateId + " could not be processed because of" +
                        "internal error";

            }


        }


    }

    @Transactional
    public void revokeCertificates(List<String> certificateIds, String userOrganizationId) {
        certificateRepository.revokeCertificates(certificateIds, userOrganizationId);
    }

    public Map<String, Long> getTotalIssuedCertificateCount(String userOrganizationId) {


        Long count = certificateRepository.countByEvent_Cluster_Organization_Id(userOrganizationId);
        Map<String, Long> countMap = Map.of("count", count);
        return countMap;
    }

    public List<CertificateInfoProjection> getLatestCertificates(int page, int size, String userOrganizationId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("issuedDate")));

        return certificateRepository.getLatestCertificateInfo(pageable, userOrganizationId).getContent();
    }

    public List<CertificateInfoProjection> getLatestCertificatesCluster(int page, int size, String userOrganizationId, String clusterId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("issuedDate")));
        return certificateRepository.getLatestCertificateInfoByCluster(pageable, clusterId, userOrganizationId).getContent();
    }

    public List<CertificateInfoProjection> getLatestCertificatesEvent(int page, int size, String userOrganizationId, String eventId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("issuedDate")));
        return certificateRepository.getLatestCertificateInfoByEvent(pageable, eventId, userOrganizationId).getContent();
    }
}
