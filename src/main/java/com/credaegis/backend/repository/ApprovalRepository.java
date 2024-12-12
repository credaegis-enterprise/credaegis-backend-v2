package com.credaegis.backend.repository;

import com.credaegis.backend.entity.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface  ApprovalRepository extends JpaRepository<Approval,String> {

    @Modifying
    @Query("UPDATE Approval a SET a.status = 'rejected' WHERE a.id = :approvalIds AND a.event.cluster.organization.id = :userOrganizationId")
    void rejectCertificates(String userOrganizationId, List<String> approvalIds);
}
