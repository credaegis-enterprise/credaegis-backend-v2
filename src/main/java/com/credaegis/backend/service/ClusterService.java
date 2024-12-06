package com.credaegis.backend.service;

import com.credaegis.backend.Constants;
import com.credaegis.backend.http.request.ClusterCreationRequest;
import com.credaegis.backend.entity.Cluster;
import com.credaegis.backend.entity.Organization;
import com.credaegis.backend.entity.Role;
import com.credaegis.backend.entity.User;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.repository.ClusterRepository;

import com.credaegis.backend.repository.OrganizationRepository;
import com.credaegis.backend.repository.RoleRepository;
import com.credaegis.backend.repository.UserRepository;
import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class ClusterService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ClusterRepository clusterRepository;
    private final OrganizationRepository organizationRepository;



    public void createCluster(ClusterCreationRequest clusterCreationRequest, String userOrganizationId) {

        Organization organization = organizationRepository.findById(userOrganizationId).
                orElseThrow(ExceptionFactory::resourceNotFound);

        User admin = userRepository.findByEmail(clusterCreationRequest.getAdminEmail());
        if (admin == null || admin.isDeleted()) {

            if (clusterRepository.findByNameAndOrganization(clusterCreationRequest.getClusterName(),
                    organization)!= null) {
                throw ExceptionFactory.customValidationError("Name already exists," +
                        "Choose a different cluster name");
            }

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
            cluster.setAdmin(user);
            cluster.setOrganization(organization);

            userRepository.save(user);
            roleRepository.save(role);
            clusterRepository.save(cluster);
        } else throw ExceptionFactory.customValidationError("The user already exists, try another email");
    }

    public void renameCluster(String clusterId, String userOrganizationId, String newName) {
        Cluster cluster = clusterRepository.findById(clusterId).orElseThrow(ExceptionFactory::resourceNotFound);



        if (cluster.getOrganization().getId().equals(userOrganizationId)) {

            if(clusterRepository.findByNameAndOrganization(newName,cluster.getOrganization())!=null)
                throw ExceptionFactory.customValidationError("Name already exists, " +
                        "Choose a different cluster name");

                 clusterRepository.renameCluster(clusterId, newName);
        } else throw ExceptionFactory.insufficientPermission();
    }


    public void deactivateCluster(String clusterId, String userOrganizationId) {

        Cluster cluster = clusterRepository.findById(clusterId).
                orElseThrow(ExceptionFactory::resourceNotFound);

        if (cluster.getDeactivated()) throw ExceptionFactory.customValidationError("Cluster already deactivated");
        if (cluster.getOrganization().getId().equals(userOrganizationId)) {
            clusterRepository.deactivateCluster(clusterId);
            userRepository.deactivateUser(userRepository.findAllUserIdByClusterId(clusterId));

        } else throw ExceptionFactory.insufficientPermission();
    }


    public void activateCluster(String clusterId, String userOrganizationId) {
        Cluster cluster = clusterRepository.findById(clusterId).
                orElseThrow(ExceptionFactory::resourceNotFound);

        if (!cluster.getDeactivated()) throw ExceptionFactory.customValidationError("Cluster already activated");
        if (cluster.getOrganization().getId().equals(userOrganizationId)) {
            clusterRepository.activateCluster(clusterId);
            userRepository.activateUser(userRepository.findAllUserIdByClusterId(clusterId));

        } else throw ExceptionFactory.insufficientPermission();
        ;

    }

    public void changeAdmin(String clusterId, String newAdminId, String userOrganizationId) {
        Cluster cluster = clusterRepository.findById(clusterId).orElseThrow(ExceptionFactory::resourceNotFound);
        User user = userRepository.findById(newAdminId).orElseThrow(ExceptionFactory::resourceNotFound);

        if (!user.getCluster().getId().equals(clusterId)) throw
                ExceptionFactory.customValidationError("New Admin must of from the same cluster");
        if (cluster.getAdmin().getId().equals(newAdminId)) throw
                ExceptionFactory.customValidationError("User is already admin of the specified cluster");

        if (cluster.getOrganization().getId().equals(userOrganizationId)) {
            clusterRepository.changeAdmin(newAdminId, clusterId);
        } else throw ExceptionFactory.insufficientPermission();


    }

    public void lockPermissions(String clusterId, String userOrganizationId) {
        Cluster cluster = clusterRepository.findById(clusterId).orElseThrow(ExceptionFactory::resourceNotFound);
        if(cluster.getLocked()) throw ExceptionFactory.customValidationError("Cluster already locked");
        if (cluster.getOrganization().getId().equals(userOrganizationId)) {

            clusterRepository.lockPermissions(clusterId);
            roleRepository.updateRole(Constants.LOCKED_CLUSTER_ADMIN, cluster.getAdmin().getId());
        }
        else throw ExceptionFactory.insufficientPermission();

    }

    public void unlockPermissions(String clusterId, String userOrganizationId) {
        Cluster cluster = clusterRepository.findById(clusterId).orElseThrow(
                ExceptionFactory::resourceNotFound );
        if(!cluster.getLocked()) throw ExceptionFactory.customValidationError("Cluster already unlocked");
        if (cluster.getOrganization().getId().equals(userOrganizationId)) {

            clusterRepository.unlockPermissions(clusterId);
            roleRepository.updateRole(Constants.CLUSTER_ADMIN, cluster.getAdmin().getId());
        }
        else throw ExceptionFactory.insufficientPermission();

    }
}
