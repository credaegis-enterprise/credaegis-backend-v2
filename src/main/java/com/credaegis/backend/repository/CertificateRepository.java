package com.credaegis.backend.repository;

import com.credaegis.backend.dto.CertificateInfoDTO;
import com.credaegis.backend.dto.projection.CertificateInfoProjection;
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



    @Query(
            "SELECT c.id AS id,c.recipientName AS recipientName,c.recipientEmail AS recipientEmail," +
                    "c.certificateName AS certificateName,c.issuedDate AS issuedDate," +
                    "c.expiryDate AS expiryDate,c.revoked AS revoked,c.revokedDate as revokedDate," +
                    "c.issuedByUser.username AS issuerName,c.issuedByUser.email AS issuerEmail," +
                    "c.comments AS comment,c.event.name AS eventName,c.event.cluster.name AS clusterName" +
                    "  FROM Certificate c WHERE c.event.cluster.organization.id = :organizationId")
    Page<CertificateInfoProjection> getLatestCertificateInfo(Pageable pageable, @Param("organizationId") String organizationId);


//    @Query(
//            "SELECT new com.credaegis.backend.dto.CertificateInfoDTO(c.id,c.recipientName,c.recipientEmail," +
//                    "c.issuedByUser.username,c.issuedByUser.email,c.issuedDate," +
//                    "c.expiryDate,c.revoked,c.revokedDate," +
//                    "c.comments,c.certificateName,c.event.name,c.event.cluster.name) " +
//                    "FROM Certificate c "
//    )
//    List<CertificateInfoDTO> getLatestCertificateInfo();


    Page<Certificate> findByEvent_Cluster_Organization_Id(String id, Pageable pageable);

    Page<Certificate> findByEvent_Cluster_IdAndEvent_Cluster_Organization_Id(String clusterId, String userOrganizationId, Pageable pageable);

    Page<Certificate> findByEvent_IdAndEvent_Cluster_Organization_Id(String eventId, String userOrganizationId, Pageable pageable);

    Long countByEvent_Cluster_Organization_Id(String userOrganizationId);
}
