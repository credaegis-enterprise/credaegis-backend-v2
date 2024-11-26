package com.credaegis.backend.repository;

import com.credaegis.backend.entity.Cluster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClusterRepository extends JpaRepository<Cluster,String> {
}
