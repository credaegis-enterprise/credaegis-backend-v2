package com.credaegis.backend.service;


import com.credaegis.backend.dto.CertificateInfoDTO;
import com.credaegis.backend.dto.projection.CertificateInfoProjection;
import com.credaegis.backend.entity.Certificate;
import com.credaegis.backend.repository.CertificateRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;

    @Transactional
    public void revokeCertificates(List<String> certificateIds, String userOrganizationId){
        certificateRepository.revokeCertificates(certificateIds,userOrganizationId);
    }

    public Map<String,Long> getTotalIssuedCertificateCount(String userOrganizationId){


        Long count = certificateRepository.countByEvent_Cluster_Organization_Id(userOrganizationId);
        Map<String,Long> countMap = Map.of("count",count);
        return countMap;
    }

    public List<CertificateInfoProjection> getLatestCertificates(int page, int size, String userOrganizationId){
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Order.desc("issuedDate")));

        return certificateRepository.getLatestCertificateInfo(pageable,userOrganizationId).getContent();
    }

    public List<CertificateInfoProjection> getLatestCertificatesCluster(int page, int size, String userOrganizationId, String clusterId){
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Order.desc("issuedDate")));
        return certificateRepository.getLatestCertificateInfoByCluster(pageable,clusterId,userOrganizationId).getContent();
    }

    public List<CertificateInfoProjection> getLatestCertificatesEvent(int page, int size, String userOrganizationId, String eventId){
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Order.desc("issuedDate")));
        return certificateRepository.getLatestCertificateInfoByEvent(pageable,eventId,userOrganizationId).getContent();
    }
}
