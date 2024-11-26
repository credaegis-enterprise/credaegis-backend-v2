package com.credaegis.backend.configuration.security.service;

import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.entity.Role;
import com.credaegis.backend.entity.User;
import com.credaegis.backend.repository.RoleRepository;
import com.credaegis.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private List<GrantedAuthority> getAuthoritesAndRoles(String userId){
        Role role = roleRepository.findByUser_id(userId);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
        return grantedAuthorities;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if(user==null)
            throw new UsernameNotFoundException("Username not found");

        return new CustomUser(user.getEmail(),
                user.getPassword(),
                getAuthoritesAndRoles(user.getId()));
    }


}
