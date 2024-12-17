package com.credaegis.backend.repository;

import com.credaegis.backend.dto.ClusterInfoDTO;
import com.credaegis.backend.entity.Cluster;
import com.credaegis.backend.entity.Organization;
import com.credaegis.backend.dto.projection.ClusterSearchProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface  ClusterRepository extends JpaRepository<Cluster,String> {

        Cluster findByNameAndOrganization(String name, Organization organization);
        List<Cluster> findByOrganization(Organization organization);


//        @Query("SELECT new com.credaegis.backend.dto.ClusterInfoDTO" +
//                "(cl.name,COUNT (a.id) AS issuedCertificateCount,COUNT (CASE WHEN c.revoked = true THEN 1) AS revokedCertificateCount," +
//                "COUNT (CASE WHEN a.status = 'rejected' THEN 1) AS rejectedCertificateCount) FROM Cluster cl INNER JOIN " +
//                "" +
//                "FROM Cluster c WHERE c.id = :id")


        @Query("SELECT c.id AS id,c.name AS name FROM Cluster c WHERE c.name LIKE %:name% AND c.organization.id = :id")
        List<ClusterSearchProjection> searchByName(@Param("name") String name, @Param("id") String userOrganizationId);

        //maps via alias (runtime)
        @Query("SELECT  c.id AS id ,c.name AS name from Cluster c WHERE c.organization.id =:id")
        List<ClusterSearchProjection> getAllNameAndId(@Param("id") String id);

        @Modifying
        @Query("UPDATE Cluster c SET c.deactivated = true WHERE c.id = :id ")
        void deactivateCluster(@Param("id") String clusterId);

        @Modifying
        @Query("UPDATE  Cluster c SET c.deactivated = false WHERE c.id = :id ")
        void activateCluster(@Param("id") String clusterId);

        @Modifying
        @Query("UPDATE Cluster c SET c.name = :name WHERE c.id = :id")
        void renameCluster(@Param("id") String clusterId, @Param("name") String newName);



        @Modifying
        @Query("UPDATE Cluster c SET c.locked = true WHERE c.id = :id")
        void lockPermissions(@Param("id") String clusterId);

        @Modifying
        @Query("UPDATE Cluster c SET c.locked = false WHERE c.id = :id")
        void unlockPermissions(@Param("id") String clusterId);

        @Query("SELECT new com.credaegis.backend.dto.ClusterInfoDTO" +
                "(c.id,c.name,c.locked,c.deactivated,c.createdOn) FROM Cluster c WHERE c = :cluster")
        ClusterInfoDTO getClusterInfo(@Param("cluster") Cluster cluster);



        Cluster findByIdAndOrganization(String id, Organization organization);
}
