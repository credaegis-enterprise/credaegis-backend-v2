package com.credaegis.backend.service.organization;


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
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final MinioClient minioClient;
    private final RabbitTemplate rabbitTemplate;





    public CertificateInfoProjection getInfoByHash(String hash,String organizationId)
    {
        return certificateRepository.getCertificateByHash(organizationId,hash);
    }

    public ViewCertificateDTO viewCertificate(String certificateId, String userOrganizationId) {
       Certificate certificate = certificateRepository.findById(certificateId).orElseThrow(ExceptionFactory::resourceNotFound);
        if (!certificate.getEvent().getCluster().getOrganization().getId().equals(userOrganizationId))
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
    public void revokeCertificates(List<String> certificateIds, String userOrganizationId) {
        certificateRepository.revokeCertificates(certificateIds, userOrganizationId);
    }

    public Map<String, Long> getTotalIssuedCertificateCount(String userOrganizationId) {


        Long count = certificateRepository.countByEvent_Cluster_Organization_Id(userOrganizationId);
        Map<String, Long> countMap = Map.of("count", count);
        return countMap;
    }

    public List<CertificateInfoProjection> getLatestCertificates(int page, int size, String userOrganizationId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("issuedDate"), Sort.Order.asc("id")));

        return certificateRepository.getLatestCertificateInfo(pageable, userOrganizationId).getContent();
    }



    public Long getIssuedCountEvent(String userOrganizationId, String eventId) {
        Long count = certificateRepository.countByEvent_IdAndEvent_Cluster_Organization_Id(eventId, userOrganizationId);
        return count;
    }

    public Long getIssuedCountCluster(String userOrganizationId, String clusterId) {
       Long count = certificateRepository.countByEvent_Cluster_IdAndEvent_Cluster_Organization_Id(clusterId, userOrganizationId);
         return count;
    }

    public List<CertificateInfoProjection> getLatestCertificatesCluster(int page, int size, String userOrganizationId, String clusterId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("issuedDate"), Sort.Order.asc("id")));
        return certificateRepository.getLatestCertificateInfoByCluster(pageable, clusterId, userOrganizationId).getContent();
    }

    public List<CertificateInfoProjection> getLatestCertificatesEvent(int page, int size, String userOrganizationId, String eventId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("issuedDate"), Sort.Order.asc("id")));
        return certificateRepository.getLatestCertificateInfoByEvent(pageable, eventId, userOrganizationId).getContent();
    }
}
