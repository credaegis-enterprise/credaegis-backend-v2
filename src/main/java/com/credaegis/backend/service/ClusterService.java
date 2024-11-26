package com.credaegis.backend.service;

import com.credaegis.backend.dto.request.ClusterCreationRequest;
import com.credaegis.backend.entity.Clusters;
import com.credaegis.backend.entity.Roles;
import com.credaegis.backend.entity.Users;
import com.credaegis.backend.repository.ClustersRepository;
import com.credaegis.backend.repository.RolesRepository;
import com.credaegis.backend.repository.UsersRepository;
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
    UsersRepository usersRepository;

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    ClustersRepository clustersRepository;

    @Transactional
    public void CreateCluster(ClusterCreationRequest clusterCreationRequest){

        Users user = new Users();
        Clusters cluster = new Clusters();
        Roles role = new Roles();
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

        usersRepository.save(user);
        rolesRepository.save(role);
        clustersRepository.save(cluster);
    }
}
