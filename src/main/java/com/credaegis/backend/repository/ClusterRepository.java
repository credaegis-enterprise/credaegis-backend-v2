package com.credaegis.backend.repository;

import com.credaegis.backend.entity.Cluster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClusterRepository extends JpaRepository<Cluster,String> {

        @Modifying
        @Query("UPDATE Cluster c SET c.deactivated = true WHERE c.id = :id ")
        int deactivateCluster(@Param("id") String clusterId);

        @Modifying
        @Query("UPDATE  Cluster c SET c.deactivated = false WHERE c.id = :id ")
        int activateCluster(@Param("id") String clusterId);
}
