package com.credaegis.backend.service;


import com.credaegis.backend.entity.Roles;
import com.credaegis.backend.entity.Users;
import com.credaegis.backend.repository.RolesRepository;
import com.credaegis.backend.repository.UsersRepository;
import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class InitializerService {


    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    RolesRepository rolesRepository;


    public void  addUserService(Users user){

        user.setId(UlidCreator.getUlid().toString());
        user.setEmail("sgce@saintgits.org");
        user.setMfaEnabled(false);
        user.setPassword(passwordEncoder.encode("sgce"));
        usersRepository.save(user);

        Roles role = new Roles();
        role.setId(UlidCreator.getUlid().toString());
        role.setRole("ROLE_ADMIN");
        role.setAuthority("ADMIN");
        role.setUser(user);
        rolesRepository.save(role);



        usersRepository.save(user);


    }
}
