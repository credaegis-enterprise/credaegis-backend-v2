package com.credaegis.backend.service.member;


import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.dto.RevocationBlockchainDTO;
import com.credaegis.backend.dto.ViewCertificateDTO;
import com.credaegis.backend.dto.projection.CertificateInfoProjection;
import com.credaegis.backend.entity.Certificate;
import com.credaegis.backend.entity.CertificateStatus;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.repository.CertificateRepository;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class MemberCertificateService {

    private final CertificateRepository certificateRepository;
    private final RabbitTemplate rabbitTemplate;
    private final MinioClient minioClient;


    public ViewCertificateDTO viewCertificate(String certificateId, String userClusterId) {
        Certificate certificate = certificateRepository.findById(certificateId).orElseThrow(ExceptionFactory::resourceNotFound);
        if (!certificate.getEvent().getCluster().getId().equals(userClusterId))
            throw ExceptionFactory.insufficientPermission();

        String approvalPath = certificate.getEvent().getCluster().getId() + "/"
                + certificate.getEvent().getId() + "/" + certificate.getId() + "/" + certificate.getCertificateName();

        try {
            InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket("approvals")
                    .object(approvalPath)
                    .build());

            ViewCertificateDTO viewCertificateDTO = new ViewCertificateDTO(stream, certificate.getCertificateName());
            return viewCertificateDTO;

        } catch (Exception e) {
            log.error(e.getMessage());
            throw ExceptionFactory.internalError();
        }


    }


    @Transactional
    public void revokeCertificatesBlockchain(List<String> certificateIds, String userClusterId,String userId) {

        for (String certificateId : certificateIds) {

            try {
                Certificate certificate = certificateRepository.findById(certificateId).orElseThrow(ExceptionFactory::resourceNotFound);
                if (!certificate.getEvent().getCluster().getId().equals(userClusterId))
                    throw ExceptionFactory.insufficientPermission();

                RevocationBlockchainDTO revocationBlockchainDTO = new RevocationBlockchainDTO();
                revocationBlockchainDTO.setRevokerId(userId);
                revocationBlockchainDTO.setCertificateId(certificateId);
                revocationBlockchainDTO.setHash(certificate.getCertificateHash());
                certificate.setStatus(CertificateStatus.buffered);
                certificateRepository.save(certificate);

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
    public void revokeCertificates(List<String> certificateIds, String userClusterId){
        certificateRepository.revokeCertificates(certificateIds,userClusterId);
    }

    public Map<String,Long> getTotalIssuedCertificateCount(String userClusterId){


        Long count = certificateRepository.countByEvent_Cluster_Id(userClusterId);
        Map<String,Long> countMap = Map.of("count",count);
        return countMap;
    }


    public Long getIssuedCountEvent(String userClusterId, String eventId) {
        Long count = certificateRepository.countByEvent_IdAndEvent_Cluster_Id(eventId,userClusterId);
        return count;
    }


    public List<CertificateInfoProjection> getLatestCertificates(int page, int size, String userClusterId){
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Order.desc("issuedDate")));

        return certificateRepository.getLatestCertificateInfoForMember(pageable,userClusterId).getContent();
    }


    public List<CertificateInfoProjection> getLatestCertificatesEvent(int page, int size, String userClusterId, String eventId){
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Order.desc("issuedDate")));
        return certificateRepository.getLatestCertificateInfoByEventForMember(pageable,eventId,userClusterId).getContent();
    }
}
