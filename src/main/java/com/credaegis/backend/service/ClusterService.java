package com.credaegis.backend.service;

import com.credaegis.backend.dto.request.ClusterCreationRequest;
import com.credaegis.backend.entity.Cluster;
import com.credaegis.backend.entity.Role;
import com.credaegis.backend.entity.User;
import com.credaegis.backend.repository.ClusterRepository;

import com.credaegis.backend.repository.RoleRepository;
import com.credaegis.backend.repository.UserRepository;
import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClusterService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ClusterRepository clusterRepository;

    @Transactional
    public void CreateCluster(ClusterCreationRequest clusterCreationRequest){

        User user = new User();
        Cluster cluster = new Cluster();
        Role role = new Role();
        user.setId(UlidCreator.getUlid().toString());
        user.setEmail(clusterCreationRequest.getAdminEmail());
        user.setPassword(passwordEncoder.encode("sgce"));

        role.setId(UlidCreator.getUlid().toString());
        role.setRole("ROLE_CLUSTERADMIN");
        role.setAuthority("MEMBER");
        role.setUser(user);


        cluster.setId(UlidCreator.getUlid().toString());
        cluster.setDeactivated(false);
        cluster.setName(clusterCreationRequest.getClusterName());
        cluster.setUser(user);

        userRepository.save(user);
        roleRepository.save(role);
        clusterRepository.save(cluster);
    }
}
