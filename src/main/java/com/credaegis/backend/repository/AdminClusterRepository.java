package com.credaegis.backend.repository;

import com.credaegis.backend.dto.AdminClusterInfoDTO;
import com.credaegis.backend.entity.AdminCluster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminClusterRepository extends JpaRepository<AdminCluster, String> {

    @Modifying
    @Query("UPDATE AdminCluster ac SET ac.user.id = :adminId WHERE ac.cluster.id =:clusterId")
    public void updateAdminCluster(String adminId, String clusterId);


    @Query("SELECT new com.credaegis.backend.dto.AdminClusterInfoDTO"+
            "(ac.user.id,ac.user.username,ac.user.email) FROM AdminCluster ac WHERE ac.cluster.id = :clusterId")
    public AdminClusterInfoDTO getAdminClusterInfo(@Param("clusterId") String clusterId);
}
