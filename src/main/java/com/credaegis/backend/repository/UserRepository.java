package com.credaegis.backend.repository;

import com.credaegis.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    User findByEmail(String email);

    @Query("SELECT u.id FROM User u WHERE u.cluster.id = :id")
    List<String> findAllUserIdByClusterId(@Param("id") String clusterId);

    @Modifying
    @Query("UPDATE User u SET u.deactivated = true WHERE u.id in :ids")
    void deactivateUser(@Param("ids") List<String> ids);

    @Modifying
    @Query("UPDATE User u SET u.deactivated = false WHERE u.id in :ids")
    void activateUser(@Param("ids") List<String> ids);

    @Modifying
    @Query("UPDATE User u SET u.deleted = true WHERE u.id = :id")
    void deleteUser(@Param("id") String memberId);
}
