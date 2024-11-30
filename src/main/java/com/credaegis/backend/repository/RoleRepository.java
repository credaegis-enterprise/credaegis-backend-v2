package com.credaegis.backend.repository;

import com.credaegis.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role,String> {

    Role findByUser_id (String id);

    @Modifying
    @Query("UPDATE Role r SET r.role = :role WHERE r.user.id = :id")
    void updateRole(@Param("role") String newRole, @Param("id") String userId);
}
