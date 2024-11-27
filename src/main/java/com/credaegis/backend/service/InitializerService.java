package com.credaegis.backend.service;


import com.credaegis.backend.entity.Role;
import com.credaegis.backend.entity.User;
import com.credaegis.backend.repository.RoleRepository;
import com.credaegis.backend.repository.UserRepository;
import com.github.f4b6a3.ulid.UlidCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class InitializerService {


    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;


    public void  addUserService(User user){

        user.setId(UlidCreator.getUlid().toString());
        user.setUsername("SAINTGITS COLLEGE OF ENGINEERING");
        user.setEmail("sgce@saintgits.org");
        user.setPassword(passwordEncoder.encode("sgce"));
        userRepository.save(user);

        Role role = new Role();
        role.setId(UlidCreator.getUlid().toString());
        role.setRole("ROLE_ADMIN");
        role.setAuthority("ADMIN");
        role.setUser(user);
        roleRepository.save(role);



        userRepository.save(user);


    }
}
