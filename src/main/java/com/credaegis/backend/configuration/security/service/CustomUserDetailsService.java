package com.credaegis.backend.configuration.security.service;

import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.entity.User;
import com.credaegis.backend.repository.RoleRepository;
import com.credaegis.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if(user==null)
            throw new UsernameNotFoundException("Username not found");

        return new CustomUser();
    }



}
