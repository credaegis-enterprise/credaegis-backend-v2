package com.credaegis.backend.repository;

import com.credaegis.backend.entity.Approvals;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalsRepository extends JpaRepository<Approvals,String> {
}
