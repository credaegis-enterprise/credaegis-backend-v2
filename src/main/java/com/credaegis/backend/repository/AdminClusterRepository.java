package com.credaegis.backend.repository;

import com.credaegis.backend.entity.AdminCluster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminClusterRepository extends JpaRepository<AdminCluster, String> {
}
