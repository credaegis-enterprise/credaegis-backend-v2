package com.credaegis.backend.repository;

import com.credaegis.backend.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface  CertificateRepository extends JpaRepository<Certificate,String> {
    Optional<Object> findByCertificateHash(String hashedValue);
}
