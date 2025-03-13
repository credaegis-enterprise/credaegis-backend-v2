package com.credaegis.backend.repository;

import com.credaegis.backend.dto.projection.CertificateInfoProjection;
import com.credaegis.backend.entity.Certificate;
import com.credaegis.backend.entity.CertificateStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface  CertificateRepository extends JpaRepository<Certificate,String> {

    //count can potentially cause problem if "buffered not excluded"


    Optional<Certificate> findByCertificateHash(String hashedValue);

    boolean existsByCertificateHash(String certificateHash);

    Long countByEvent_Cluster_Id(String userClusterId);

    Long countByEvent_IdAndEvent_Cluster_Id(String eventId, String userClusterId);




    @Query(
            "SELECT c.id AS id,c.recipientName AS recipientName,c.recipientEmail AS recipientEmail," +
                    "c.certificateName AS certificateName,c.issuedDate AS issuedDate," +
                    "c.expiryDate AS expiryDate,c.revoked AS revoked,c.revokedDate as revokedDate," +
                    "c.issuedByUser.username AS issuerName,c.issuedByUser.email AS issuerEmail,c.persisted AS persisted,c.status AS status," +
                    "c.comments AS comment,c.event.name AS eventName,c.event.cluster.name AS clusterName" +
                    "  FROM Certificate c WHERE c.event.cluster.organization.id = :organizationId AND c.status != 'buffered' AND c.certificateHash = :hash")
    CertificateInfoProjection getCertificateByHash(@Param("organizationId") String organizationId, @Param("hash") String hash);





    @Modifying
    @Query("UPDATE Certificate c SET c.batchInfo.id = :batchId,c.status =:status WHERE c.certificateHash IN :ids")
    void updateBatchInfo(@Param("batchId") Integer batchId, @Param("ids") List<String> certificateHashes,
                         @Param("status")CertificateStatus status);

    @Modifying
    @Query("UPDATE Certificate c SET c.revoked = true, c.revokedDate = CURRENT_DATE WHERE c.id IN :ids AND c.event.cluster.organization.id = :organizationId")
    void revokeCertificates(@Param("ids") List<String> certificateIds,@Param("organizationId") String organizationId);

    @Query(
            "SELECT c.id AS id,c.recipientName AS recipientName,c.recipientEmail AS recipientEmail," +
                    "c.certificateName AS certificateName,c.issuedDate AS issuedDate," +
                    "c.expiryDate AS expiryDate,c.revoked AS revoked,c.revokedDate as revokedDate," +
                    "c.issuedByUser.username AS issuerName,c.issuedByUser.email AS issuerEmail," +
                    "c.comments AS comment,c.event.name AS eventName,c.event.cluster.name AS clusterName,c.persisted AS persisted,c.status AS status" +
                    "  FROM Certificate c WHERE c.event.cluster.id = :id AND c.status != 'buffered'")
    Page<CertificateInfoProjection> getLatestCertificateInfoForMember(Pageable pageable, @Param("id") String clusterId);



    @Query(
            "SELECT c.id AS id,c.recipientName AS recipientName,c.recipientEmail AS recipientEmail," +
                    "c.certificateName AS certificateName,c.issuedDate AS issuedDate," +
                    "c.expiryDate AS expiryDate,c.revoked AS revoked,c.revokedDate as revokedDate," +
                    "c.issuedByUser.username AS issuerName,c.issuedByUser.email AS issuerEmail," +
                    "c.comments AS comment,c.event.name AS eventName,c.event.cluster.name AS clusterName, c.persisted AS persisted,c.status AS status" +
                    "  FROM Certificate c WHERE c.event.id = :eventId AND c.event.cluster.id = :clusterId AND c.status != 'buffered'")
    Page<CertificateInfoProjection> getLatestCertificateInfoByEventForMember(Pageable pageable, String eventId, String clusterId);



    @Query(
            "SELECT c.id AS id,c.recipientName AS recipientName,c.recipientEmail AS recipientEmail," +
                    "c.certificateName AS certificateName,c.issuedDate AS issuedDate," +
                    "c.expiryDate AS expiryDate,c.revoked AS revoked,c.revokedDate as revokedDate," +
                    "c.issuedByUser.username AS issuerName,c.issuedByUser.email AS issuerEmail,c.persisted AS persisted ,c.status AS status," +
                    "c.comments AS comment,c.event.name AS eventName,c.event.cluster.name AS clusterName" +
                    "  FROM Certificate c WHERE c.event.cluster.organization.id = :organizationId AND c.status != 'buffered'")
    Page<CertificateInfoProjection> getLatestCertificateInfo(Pageable pageable, @Param("organizationId") String organizationId);


    @Query(
            "SELECT c.id AS id,c.recipientName AS recipientName,c.recipientEmail AS recipientEmail," +
                    "c.certificateName AS certificateName,c.issuedDate AS issuedDate," +
                    "c.expiryDate AS expiryDate,c.revoked AS revoked,c.revokedDate as revokedDate," +
                    "c.issuedByUser.username AS issuerName,c.issuedByUser.email AS issuerEmail, c.persisted AS persisted, c.status AS status," +
                    "c.comments AS comment,c.event.name AS eventName,c.event.cluster.name AS clusterName" +
                    "  FROM Certificate c WHERE c.event.cluster.id = :clusterId AND c.event.cluster.organization.id = :organizationId AND c.status != 'buffered'")
    Page<CertificateInfoProjection> getLatestCertificateInfoByCluster(Pageable pageable, String clusterId, String organizationId);



    @Query(
            "SELECT c.id AS id,c.recipientName AS recipientName,c.recipientEmail AS recipientEmail," +
                    "c.certificateName AS certificateName,c.issuedDate AS issuedDate," +
                    "c.expiryDate AS expiryDate,c.revoked AS revoked,c.revokedDate as revokedDate," +
                    "c.issuedByUser.username AS issuerName,c.issuedByUser.email AS issuerEmail,c.persisted AS persisted, c.status AS status," +
                    "c.comments AS comment,c.event.name AS eventName,c.event.cluster.name AS clusterName" +
                    "  FROM Certificate c WHERE c.event.id = :eventId AND c.event.cluster.organization.id = :organizationId AND c.status != 'buffered'")
    Page<CertificateInfoProjection> getLatestCertificateInfoByEvent(Pageable pageable, String eventId, String organizationId);

    Long countByEvent_Cluster_Organization_Id(String userOrganizationId);

    Long countByEvent_Cluster_IdAndEvent_Cluster_Organization_Id(String clusterId, String userOrganizationId);

    Long countByEvent_IdAndEvent_Cluster_Organization_Id(String eventId, String userOrganizationId);
}
