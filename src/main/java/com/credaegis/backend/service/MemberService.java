package com.credaegis.backend.service;


import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.entity.Role;
import com.credaegis.backend.http.request.MemberCreationRequest;
import com.credaegis.backend.entity.Cluster;
import com.credaegis.backend.entity.User;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.repository.ClusterRepository;
import com.credaegis.backend.repository.OrganizationRepository;
import com.credaegis.backend.repository.RoleRepository;
import com.credaegis.backend.repository.UserRepository;
import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Transactional
@Service
@AllArgsConstructor
public class MemberService {

    private final UserRepository userRepository;
    private final ClusterRepository clusterRepository;
    private final OrganizationRepository organizationRepository;
    private final RoleRepository roleRepository;


    public void createMember(MemberCreationRequest memberCreationRequest, String userOrganizationId) {

        Cluster cluster = clusterRepository.findById(memberCreationRequest.getClusterId()).orElseThrow(ExceptionFactory::resourceNotFound);
        Optional<User> optionalUser = userRepository.findByEmail(memberCreationRequest.getEmail());
        if(optionalUser.isPresent()){
            if(!optionalUser.get().isDeleted())
                throw ExceptionFactory.customValidationError("User already exists, try another email");
        }


        if (cluster.getOrganization().getId().equals(userOrganizationId)) {

                User user = new User();
                Role role = new Role();
                user.setId(UlidCreator.getUlid().toString());
                user.setPassword("sgce");
                user.setUsername(memberCreationRequest.getUsername());
                user.setEmail(memberCreationRequest.getEmail());
                user.setCluster(cluster);
                role.setId(UlidCreator.getUlid().toString());
                role.setUser(user);
                role.setRole("ROLE_"+Constants.MEMBER);
                user.setOrganization(organizationRepository.findById(userOrganizationId).orElseThrow(ExceptionFactory::resourceNotFound));

                userRepository.save(user);
                roleRepository.save(role);


        } else throw ExceptionFactory.insufficientPermission();


    }

    public void activateMember(String memberId, String userId, String userOrganizationId) {
        User user = userRepository.findById(memberId).orElseThrow(ExceptionFactory::resourceNotFound);
        if (!(user.getOrganization().getId().equals(userOrganizationId)))
            throw ExceptionFactory.insufficientPermission();
        if (userId.equals(memberId)) {
            throw ExceptionFactory.customValidationError("You cannot activate yourself");
        }
        if (user.getRole().getRole().equals("ROLE_" + Constants.CLUSTER_ADMIN))
            throw ExceptionFactory.customValidationError("The member is an  cluster admin, you cannot perform this operation");
        if (!user.getDeactivated()) throw ExceptionFactory.customValidationError("User already activated");

        //checks whether admin and the said member are in same organization

        userRepository.activateUser(new ArrayList<>(List.of(memberId)));


    }


    public void deactivateMember(String memberId, String userId, String userOrganizationId) {

        User user = userRepository.findById(memberId).orElseThrow(ExceptionFactory::resourceNotFound);
        if (!user.getOrganization().getId().equals(userOrganizationId))
            throw ExceptionFactory.insufficientPermission();
        if (userId.equals(memberId)) {
            throw ExceptionFactory.customValidationError("You cannot deactivate yourself");
        }

        if (user.getRole().getRole().equals("ROLE_" + Constants.CLUSTER_ADMIN))
            throw ExceptionFactory.customValidationError("The member is an  cluster admin, you cannot perform this operation");

        if (!user.getDeactivated()) throw ExceptionFactory.customValidationError("User already deactivated");

        userRepository.deactivateUser(new ArrayList<>(List.of(memberId)));
    }


    public void deleteMember(String memberId, String userId, String userOrganizationId) {
        User user = userRepository.findById(memberId).orElseThrow(ExceptionFactory::resourceNotFound);
        if (!user.getOrganization().getId().equals(userOrganizationId))
            throw ExceptionFactory.insufficientPermission();
        if (userId.equals(memberId)) {
            throw ExceptionFactory.customValidationError("You cannot delete yourself");
        }


        if (user.getRole().getRole().equals("ROLE_" + Constants.CLUSTER_ADMIN))
            throw ExceptionFactory.customValidationError("The member is an  cluster admin, you cannot perform this operation");

        userRepository.deleteUser(memberId);

    }

    public void renameUser(String memberId, String newName, String userOrganizationId) {
        User user = userRepository.findById(memberId).orElseThrow(ExceptionFactory::resourceNotFound);
        if (!user.getOrganization().getId().equals(userOrganizationId))
            throw ExceptionFactory.insufficientPermission();
        userRepository.renameUser(user.getId(), newName);


    }
}
