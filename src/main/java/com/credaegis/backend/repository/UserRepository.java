package com.credaegis.backend.repository;

import com.credaegis.backend.entity.Role;
import com.credaegis.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    User findByEmail(String email);

}
