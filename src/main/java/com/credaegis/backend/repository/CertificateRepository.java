package com.credaegis.backend.repository;

import com.credaegis.backend.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  CertificateRepository extends JpaRepository<Certificate,String> {
}
