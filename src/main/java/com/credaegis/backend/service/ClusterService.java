package com.credaegis.backend.service;

import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.entity.*;
import com.credaegis.backend.http.request.ClusterCreationRequest;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.http.response.custom.ClusterInfoResponse;
import com.credaegis.backend.dto.projection.ClusterSearchProjection;
import com.credaegis.backend.repository.*;

import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class ClusterService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ClusterRepository clusterRepository;
    private final AdminClusterRepository adminClusterRepository;
    private final OrganizationRepository organizationRepository;
    private final EventRepository eventRepository;


    public void createCluster(ClusterCreationRequest clusterCreationRequest, String organizationId) {

        Organization organization = organizationRepository.findById(organizationId).orElseThrow(
                ExceptionFactory::resourceNotFound
        );
        Optional<User> optionalUser = userRepository.findByEmail(clusterCreationRequest.getAdminEmail());

        if (optionalUser.isPresent()) {
            if (!optionalUser.get().isDeleted())
                throw ExceptionFactory.customValidationError("User already exists, try another email");
        }

        if (clusterRepository.findByNameAndOrganization(clusterCreationRequest.getClusterName(),
                organization) != null) {
            throw ExceptionFactory.customValidationError("Name already exists," +
                    "Choose a different cluster name");
        }

        User user = new User();
        Cluster cluster = new Cluster();
        Role role = new Role();
        AdminCluster adminCluster = new AdminCluster();
        user.setId(UlidCreator.getUlid().toString());
        user.setEmail(clusterCreationRequest.getAdminEmail());
        user.setPassword(passwordEncoder.encode("sgce"));
        user.setOrganization(organization);
        user.setUsername(clusterCreationRequest.getAdminName());

        role.setId(UlidCreator.getUlid().toString());
        role.setRole("ROLE_CLUSTER_ADMIN");
        role.setUser(user);

        cluster.setId(UlidCreator.getUlid().toString());
        cluster.setDeactivated(false);
        cluster.setName(clusterCreationRequest.getClusterName());
        cluster.setOrganization(organization);


        user.setCluster(cluster);

        adminCluster.setId(UlidCreator.getUlid().toString());
        adminCluster.setCluster(cluster);

        adminCluster.setUser(user);

        clusterRepository.save(cluster);
        userRepository.save(user);
        roleRepository.save(role);
        adminClusterRepository.save(adminCluster);

    }


    public List<ClusterSearchProjection> searchCluster(String userOrganizationId, String name) {
        return clusterRepository.searchByName(name, userOrganizationId);
    }

    public void renameCluster(String clusterId, String userOrganizationId, String newName) {
        Cluster cluster = clusterRepository.findById(clusterId).orElseThrow(ExceptionFactory::resourceNotFound);


        if (cluster.getOrganization().getId().equals(userOrganizationId)) {
            if(cluster.getName().equals(newName))
                throw ExceptionFactory.customValidationError("Cannot enter the same name");
            if (clusterRepository.findByNameAndOrganization(newName, cluster.getOrganization()) != null)
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
    }


    //can only be changed to a member of same cluster,
    public void changeAdmin(String clusterId, String newAdminId, String userOrganizationId) {
        Cluster cluster = clusterRepository.findById(clusterId).orElseThrow(ExceptionFactory::resourceNotFound);
        User user = userRepository.findByIdAndDeleted(newAdminId, false).orElseThrow(
                ExceptionFactory::resourceNotFound
        );

        if (!user.getCluster().getId().equals(clusterId)) throw
                ExceptionFactory.customValidationError("New AdminCluster must of from the same cluster");
        if (cluster.getAdminCluster().getUser().getId().equals(newAdminId)) throw
                ExceptionFactory.customValidationError("User is already admin of the specified cluster");
        if(user.getDeactivated())
            throw ExceptionFactory.customValidationError("User is deactivated, you must activate to make admin");

        if (cluster.getOrganization().getId().equals(userOrganizationId)) {
            cluster.getAdminCluster().getUser().getRole().setRole("ROLE_"+Constants.MEMBER);
            user.getRole().setRole("ROLE_"+Constants.CLUSTER_ADMIN);
            userRepository.save(user);
            clusterRepository.save(cluster);
            adminClusterRepository.updateAdminCluster(newAdminId, clusterId);

        } else throw ExceptionFactory.insufficientPermission();


    }


    public void lockPermissions(String clusterId, String userOrganizationId) {
        Cluster cluster = clusterRepository.findById(clusterId).orElseThrow(ExceptionFactory::resourceNotFound);
        if (cluster.getLocked()) throw ExceptionFactory.customValidationError("Cluster already locked");
        if (cluster.getOrganization().getId().equals(userOrganizationId)) {

            clusterRepository.lockPermissions(clusterId);
            roleRepository.updateRole("ROLE_"+Constants.LOCKED_CLUSTER_ADMIN, cluster.getAdminCluster().getId());
        } else throw ExceptionFactory.insufficientPermission();

    }

    public void unlockPermissions(String clusterId, String userOrganizationId) {
        Cluster cluster = clusterRepository.findById(clusterId).orElseThrow(
                ExceptionFactory::resourceNotFound);
        if (!cluster.getLocked()) throw ExceptionFactory.customValidationError("Cluster already unlocked");
        if (cluster.getOrganization().getId().equals(userOrganizationId)) {

            clusterRepository.unlockPermissions(clusterId);
            roleRepository.updateRole("ROLE_"+Constants.CLUSTER_ADMIN, cluster.getAdminCluster().getId());
        } else throw ExceptionFactory.insufficientPermission();

    }


    public List<ClusterSearchProjection> getAllNameAndId(String organizationId) {
        return clusterRepository.getAllNameAndId(organizationId);
    }

    public List<Cluster> getAllClusters(String organizationId) {
        return clusterRepository.findByOrganization(organizationRepository.findById(organizationId).orElseThrow(
                ExceptionFactory::resourceNotFound
        ));
    }

    public ClusterInfoResponse getOneCluster(String organizationId, String clusterId) {

        System.out.println(clusterId);
        Cluster cluster = clusterRepository.findById(clusterId).orElseThrow(ExceptionFactory::resourceNotFound);
        if(!cluster.getOrganization().getId().equals(organizationId)) throw ExceptionFactory.insufficientPermission();
        ClusterInfoResponse clusterInfoResponse = new ClusterInfoResponse();
        clusterInfoResponse.setAdminInfo(adminClusterRepository.getAdminClusterInfo(cluster));
        clusterInfoResponse.setClusterInfo(clusterRepository.getClusterInfo(cluster));
        clusterInfoResponse.setEvents(eventRepository.getEventInfo(cluster));
        clusterInfoResponse.setMembers(userRepository.getMemberInfo(cluster));
        return clusterInfoResponse;
    }


}
