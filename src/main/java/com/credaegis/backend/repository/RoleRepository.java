package com.credaegis.backend.repository;

import com.credaegis.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,String> {

    Role findByUser_id (String id);
}
