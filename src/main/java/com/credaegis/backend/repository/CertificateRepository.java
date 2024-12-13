package com.credaegis.backend.repository;

import com.credaegis.backend.entity.Certificate;
import com.credaegis.backend.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface  CertificateRepository extends JpaRepository<Certificate,String> {
    Optional<Certificate> findByCertificateHash(String hashedValue);

    Page<Certificate> findByEvent_Cluster_Organization_Id(String id, Pageable pageable);
}
