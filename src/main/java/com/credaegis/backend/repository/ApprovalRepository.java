package com.credaegis.backend.repository;

import com.credaegis.backend.entity.Approval;
import com.credaegis.backend.entity.ApprovalStatus;
import com.credaegis.backend.entity.Cluster;
import com.credaegis.backend.entity.Event;
import com.credaegis.backend.dto.projection.ApprovalInfoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApprovalRepository extends JpaRepository<Approval, String> {



    Long countByEvent_Cluster_Organization_IdAndStatus(String organizationId, ApprovalStatus approvalStatus);


    @Modifying
    @Query("UPDATE Approval a SET a.status = 'rejected' WHERE a.id in :appIds AND a.event.cluster.organization.id = :id")
    void rejectCertificates(@Param("id") String userOrganizationId, @Param("appIds") List<String> approvalIds);


    @Query("SELECT a.id AS id,a.approvalCertificateName AS approvalCertificateName," +
            "a.recipientName  AS recipientName,a.recipientEmail AS recipientEmail," +
            "a.expiryDate AS expiryDate,a.comments AS comment,a.status AS status," +
            "a.createdOn AS createdOn,a.updatedOn AS updatedOn," +
            "a.event.cluster.name AS clusterName,a.event.cluster.organization.name AS organizationName," +
            "a.event.name AS eventName,a.event.id AS eventId,a.event.cluster.id AS clusterId " +
            "FROM Approval a WHERE  a.status = :status AND a.event.cluster.organization.id = :userOrganizationId")
    List<ApprovalInfoProjection> getApprovalInfo(@Param("status")
                                                 ApprovalStatus status, @Param("userOrganizationId") String userOrganizationId);


    @Query("SELECT a.id AS id,a.approvalCertificateName AS approvalCertificateName," +
            "a.recipientName  AS recipientName,a.recipientEmail AS recipientEmail," +
            "a.expiryDate AS expiryDate,a.comments AS comment,a.status AS status," +
            "a.createdOn AS createdOn,a.updatedOn AS updatedOn," +
            "a.event.cluster.name AS clusterName,a.event.cluster.organization.name AS organizationName," +
            "a.event.name AS eventName,a.event.id AS eventId,a.event.cluster.id AS clusterId " +
            "FROM Approval a WHERE a.event.cluster = :cluster AND a.status = :status")
    List<ApprovalInfoProjection> getApprovalInfoByClusterAndStatus(Cluster cluster, ApprovalStatus status);


    @Query("SELECT a.id AS id,a.approvalCertificateName AS approvalCertificateName," +
            "a.recipientName  AS recipientName,a.recipientEmail AS recipientEmail," +
            "a.expiryDate AS expiryDate,a.comments AS comment,a.status AS status," +
            "a.createdOn AS createdOn,a.updatedOn AS updatedOn," +
            "a.event.cluster.name AS clusterName,a.event.cluster.organization.name AS organizationName," +
            "a.event.name AS eventName,a.event.id AS eventId,a.event.cluster.id AS clusterId " +
            "FROM Approval a WHERE a.event = :event AND a.status = :status")
    List<ApprovalInfoProjection> getApprovalInfoByEventAndStatus(Event event, ApprovalStatus status);


}
