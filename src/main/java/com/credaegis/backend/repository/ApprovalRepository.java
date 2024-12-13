package com.credaegis.backend.repository;

import com.credaegis.backend.entity.Approval;
import com.credaegis.backend.entity.Cluster;
import com.credaegis.backend.entity.Event;
import com.credaegis.backend.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface  ApprovalRepository extends JpaRepository<Approval,String> {

    @Modifying
    @Query("UPDATE Approval a SET a.status = 'rejected' WHERE a.id in :appIds AND a.event.cluster.organization.id = :id")
    void rejectCertificates(@Param("id") String userOrganizationId, @Param("appIds") List<String> approvalIds);

    List<Approval> findByEventAndStatus(Event event, Status status);
    List<Approval> findByClusterAndStatus(Cluster cluster, Status status);
}
