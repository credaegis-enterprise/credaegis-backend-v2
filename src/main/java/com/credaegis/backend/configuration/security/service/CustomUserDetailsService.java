package com.credaegis.backend.configuration.security.service;

import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.entity.Role;
import com.credaegis.backend.entity.User;
import com.credaegis.backend.repository.RoleRepository;
import com.credaegis.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    private List<GrantedAuthority> getAuthoritesAndRoles(String userId) {
        Role role = roleRepository.findByUser_id(userId);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
        return grantedAuthorities;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("email not found"));

        String clusterId = Optional.ofNullable(user.getCluster()).map(cluster -> cluster.getId()).orElse(null);
        return new CustomUser(getAuthoritesAndRoles(user.getId()),user.getId(),user.getEmail(),
                user.getOrganization().getId(),user.getPassword(),user.getMfaEnabled(),clusterId);

    }


}
