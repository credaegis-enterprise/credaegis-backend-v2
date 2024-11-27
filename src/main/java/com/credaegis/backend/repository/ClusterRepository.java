package com.credaegis.backend.repository;

import com.credaegis.backend.entity.Cluster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ClusterRepository extends JpaRepository<Cluster,String> {

        @Modifying
        @Query("UPDATE Cluster c SET c.deactivated = true WHERE c.id = ?1 ")
        int deactivateCluster(String clusterId);

        @Modifying
        @Query("UPDATE  Cluster c SET c.deactivated = false WHERE c.id = ?1 ")
        int activateCluster(String clusterId);
}
