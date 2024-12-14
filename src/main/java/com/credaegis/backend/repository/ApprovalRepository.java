package com.credaegis.backend.repository;

import com.credaegis.backend.entity.Approval;
import com.credaegis.backend.entity.Cluster;
import com.credaegis.backend.entity.Event;
import com.credaegis.backend.entity.Status;
import com.credaegis.backend.http.response.custom.ApprovalInfoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApprovalRepository extends JpaRepository<Approval, String> {

    @Modifying
    @Query("UPDATE Approval a SET a.status = 'rejected' WHERE a.id in :appIds AND a.event.cluster.organization.id = :id")
    void rejectCertificates(@Param("id") String userOrganizationId, @Param("appIds") List<String> approvalIds);


    @Query("SELECT a.id AS id,a.approvalCertificateName AS approvalCertificateName," +
            "a.recipientName  AS recipientName,a.recipientEmail AS recipientEmail," +
            "a.expiryDate AS expiryDate,a.comments AS comment,a.status AS status," +
            "a.createdOn AS createdOn,a.updatedOn AS updatedOn," +
            "a.event.cluster.name AS clusterName,a.event.cluster.organization.name AS organizationName " +
            "FROM Approval a WHERE  a.status = :status AND a.event.cluster.organization.id = :userOrganizationId")
    List<ApprovalInfoResponse> getApprovalInfo( @Param("status")
    Status status, @Param("userOrganizationId") String userOrganizationId);

    List<Approval> findByEventAndStatus(Event event, Status status);

    @Query("SELECT a FROM Approval a WHERE a.event.cluster = :cluster AND a.status =:status")
    List<Approval> findByClusterAndStatus(@Param("cluster") Cluster cluster, @Param("status") Status status);

    @Query("SELECT a FROM Approval a WHERE a.event.cluster.organization.id = :id")
    List<Approval> findByOrganizationId(@Param("id") String id);
}
