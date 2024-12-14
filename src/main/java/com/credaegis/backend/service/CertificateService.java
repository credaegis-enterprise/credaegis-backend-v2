package com.credaegis.backend.service;


import com.credaegis.backend.entity.Certificate;
import com.credaegis.backend.repository.CertificateRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    public Page<Certificate> getLatestCertificates(int page, int size, String userOrganizationId){
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Order.desc("issuedDate")));
        return certificateRepository.findByEvent_Cluster_Organization_Id(userOrganizationId,pageable);
    }

    public Page<Certificate> getLatestCertificatesCluster(int page, int size, String userOrganizationId, String clusterId){
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Order.desc("issuedDate")));
        return certificateRepository.findByEvent_Cluster_IdAndEvent_Cluster_Organization_Id(clusterId,userOrganizationId,pageable);
    }

    public Page<Certificate> getLatestCertificatesEvent(int page, int size, String userOrganizationId, String eventId){
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Order.desc("issuedDate")));
        return certificateRepository.findByEvent_IdAndEvent_Cluster_Organization_Id(eventId,userOrganizationId,pageable);
    }
}
