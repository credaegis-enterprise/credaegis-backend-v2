package com.credaegis.backend.repository;

import com.credaegis.backend.dto.MemberInfoDTO;
import com.credaegis.backend.dto.projection.MemberInfoProjection;
import com.credaegis.backend.entity.Cluster;
import com.credaegis.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findByEmail(String email);
    Optional<User> findByIdAndDeleted(String id,Boolean deleted);

    Optional<User> findByOrganization_IdAndRole_role(String userOrganizationId, String role);

    @Query("SELECT new com.credaegis.backend.dto.MemberInfoDTO(u.id, u.username, u.email, u.deactivated, u.createdOn)" +
            " FROM User u WHERE u.cluster = :cluster AND u.deleted = false")
    List<MemberInfoDTO> getMemberInfo(@Param("cluster") Cluster cluster);

    @Query("SELECT u.id FROM User u WHERE u.cluster.id = :id AND u.deleted = false ")
    List<String> findAllUserIdByClusterId(@Param("id") String clusterId);

    @Modifying
    @Query("UPDATE User u SET u.password =:newPassword WHERE u.id = :id")
    void updatePassword(@Param("id") String id, @Param("newPassword") String newPassword);


    @Modifying
    @Query("UPDATE User u SET u.mfaSecret= :secret WHERE u.id =:id")
    void updateMfaSecret(@Param("secret") String secret,@Param("id") String id);

    @Modifying
    @Query("UPDATE User u SET u.mfaEnabled = :enable WHERE u.id = :id")
    void enableMfa(@Param("enable") Boolean enable,@Param("id") String id);


    @Modifying
    @Query("UPDATE User u SET u.deactivated = true WHERE u.id in :ids")
    void deactivateUser(@Param("ids") List<String> ids);

    @Modifying
    @Query("UPDATE User u SET u.deactivated = false WHERE u.id in :ids")
    void activateUser(@Param("ids") List<String> ids);

    @Modifying
    @Query("UPDATE User u SET u.deleted = true WHERE u.id = :id")
    void deleteUser(@Param("id") String memberId);

    @Modifying
    @Query("UPDATE User u SET u.username = :name WHERE u.id = :id")
    void renameUser(@Param("name") String name, @Param("id") String userId);


    @Query("SELECT u.id AS id, u.username AS username, u.email AS email, u.deactivated AS deactivated" +
            ", u.createdOn AS createdOn,u.updatedOn AS updatedOn FROM User u WHERE u.cluster.id = :clusterId" +
            " AND u.id != :userId AND u.deleted = false")
    List<MemberInfoProjection> getAllMembers(@Param("clusterId") String clusterId, @Param("userId") String userId);

}
