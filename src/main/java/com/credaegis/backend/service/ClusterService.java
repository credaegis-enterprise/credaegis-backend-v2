package com.credaegis.backend.service;

import com.credaegis.backend.dto.request.ClusterCreationRequest;
import com.credaegis.backend.entity.Cluster;
import com.credaegis.backend.entity.Organization;
import com.credaegis.backend.entity.Role;
import com.credaegis.backend.entity.User;
import com.credaegis.backend.repository.ClusterRepository;

import com.credaegis.backend.repository.OrganizationRepository;
import com.credaegis.backend.repository.RoleRepository;
import com.credaegis.backend.repository.UserRepository;
import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class ClusterService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ClusterRepository clusterRepository;
    private final OrganizationRepository organizationRepository;


    public void createCluster(ClusterCreationRequest clusterCreationRequest,String organizationId){

        Organization organization = organizationRepository.findById(organizationId).orElseThrow(()->new RuntimeException("d"));

        User user = new User();
        Cluster cluster = new Cluster();
        Role role = new Role();
        user.setId(UlidCreator.getUlid().toString());
        user.setEmail(clusterCreationRequest.getAdminEmail());
        user.setPassword(passwordEncoder.encode("sgce"));
        user.setOrganization(organization);
        user.setUsername(clusterCreationRequest.getAdminName());

        role.setId(UlidCreator.getUlid().toString());
        role.setRole("ROLE_CLUSTERADMIN");
        role.setUser(user);


        cluster.setId(UlidCreator.getUlid().toString());
        cluster.setDeactivated(false);
        cluster.setName(clusterCreationRequest.getClusterName());
        cluster.setUser(user);
        cluster.setOrganization(organization);

        userRepository.save(user);
        roleRepository.save(role);
        clusterRepository.save(cluster);
    }


    public void deactivateCluster(String clusterId,String userOrganizationId){

            Cluster cluster = clusterRepository.findById(clusterId).
                    orElseThrow(()-> new RuntimeException("doesnt exist"));

            if(cluster.getDeactivated()) throw  new RuntimeException("Already deactivated");
            if(cluster.getOrganization().getId().equals(userOrganizationId)){
                clusterRepository.deactivateCluster(clusterId);
                userRepository.deactivateUser(userRepository.findAllUserIdByClusterId(clusterId));

            }
            else throw new RuntimeException("You dont have access");
    }


    public  void activateCluster(String clusterId, String userOrganizationId){
        Cluster cluster = clusterRepository.findById(clusterId).
                orElseThrow(()-> new RuntimeException("doesnt exist"));

        if(!cluster.getDeactivated()) throw  new RuntimeException("Already activated");
        if(cluster.getOrganization().getId().equals(userOrganizationId)){
            clusterRepository.activateCluster(clusterId);
            userRepository.activateUser(userRepository.findAllUserIdByClusterId(clusterId));

        }
        else throw new RuntimeException("You dont have access");

    }
}
