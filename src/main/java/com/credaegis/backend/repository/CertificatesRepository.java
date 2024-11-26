package com.credaegis.backend.repository;

import com.credaegis.backend.entity.Certificates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificatesRepository extends JpaRepository<Certificates,String> {
}
