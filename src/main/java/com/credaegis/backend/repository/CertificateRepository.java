package com.credaegis.backend.repository;

import com.credaegis.backend.entity.Certificate;
import com.credaegis.backend.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface  CertificateRepository extends JpaRepository<Certificate,String> {
    Optional<Certificate> findByCertificateHash(String hashedValue);

    @Modifying
    @Query("UPDATE Certificate c SET c.revoked = true, c.revokedDate = CURRENT_DATE WHERE c.id IN :ids AND c.event.cluster.organization.id = :organizationId")
    void revokeCertificates(@Param("ids") List<String> certificateIds,@Param("organizationId") String organizationId);

    Page<Certificate> findByEvent_Cluster_Organization_Id(String id, Pageable pageable);

    Page<Certificate> findByEvent_Cluster_IdAndEvent_Cluster_Organization_Id(String clusterId, String userOrganizationId, Pageable pageable);

    Page<Certificate> findByEvent_IdAndEvent_Cluster_Organization_Id(String eventId, String userOrganizationId, Pageable pageable);

    Long countByEvent_Cluster_Organization_Id(String userOrganizationId);
}
