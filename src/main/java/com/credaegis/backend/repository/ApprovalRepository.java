package com.credaegis.backend.repository;

import com.credaegis.backend.entity.Approval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  ApprovalRepository extends JpaRepository<Approval,String> {
}
