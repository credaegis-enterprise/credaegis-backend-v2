package com.credaegis.backend.service.member;


import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.dto.projection.MemberInfoProjection;
import com.credaegis.backend.entity.Cluster;
import com.credaegis.backend.entity.Role;
import com.credaegis.backend.entity.User;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.http.request.MemberCreationRequest;
import com.credaegis.backend.http.response.custom.MemberInfoResponse;
import com.credaegis.backend.repository.ClusterRepository;
import com.credaegis.backend.repository.OrganizationRepository;
import com.credaegis.backend.repository.RoleRepository;
import com.credaegis.backend.repository.UserRepository;
import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Transactional
public class MemberClusterService {


    private final UserRepository userRepository;
    private final ClusterRepository clusterRepository;
    private final OrganizationRepository organizationRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;



    public MemberInfoResponse getMembers(String userClusterId, String userId) {

            Cluster cluster = clusterRepository.findById(userClusterId).orElseThrow(ExceptionFactory::resourceNotFound);
            List<MemberInfoProjection> members = userRepository.getAllMembers(userClusterId, userId);
            return MemberInfoResponse
                    .builder()
                    .members(members)
                    .clusterAdminName(cluster.getAdminCluster().getUser().getUsername())
                    .clusterAdminEmail(cluster.getAdminCluster().getUser().getEmail())
                    .clusterName(cluster.getName())
                    .clusterId(cluster.getId())
                    .build();
    }


    public void createMember(MemberCreationRequest memberCreationRequest, String userClusterId, String userOrganizationId) {

        Cluster cluster = clusterRepository.findById(userClusterId).orElseThrow(ExceptionFactory::resourceNotFound);
        Optional<User> optionalUser = userRepository.findByEmail(memberCreationRequest.getEmail());
        if (optionalUser.isPresent()) {
            if (!optionalUser.get().isDeleted())
                throw ExceptionFactory.customValidationError("User already exists, try another email");
        }


        User user = new User();
        Role role = new Role();
        user.setId(UlidCreator.getUlid().toString());
        user.setPassword(passwordEncoder.encode("sgce"));
        user.setUsername(memberCreationRequest.getUsername());
        user.setEmail(memberCreationRequest.getEmail());
        user.setCluster(cluster);
        role.setId(UlidCreator.getUlid().toString());
        role.setUser(user);
        role.setRole("ROLE_" + Constants.MEMBER);
        user.setOrganization(organizationRepository.findById(userOrganizationId).orElseThrow(ExceptionFactory::resourceNotFound));

        userRepository.save(user);
        roleRepository.save(role);


    }

    public void deactivateMember(String memberId, String userId, String userClusterId) {

        User user = userRepository.findById(memberId).orElseThrow(ExceptionFactory::resourceNotFound);
        if (!user.getCluster().getId().equals(userClusterId))
            throw ExceptionFactory.insufficientPermission();

        if (userId.equals(memberId)) {
            throw ExceptionFactory.customValidationError("You cannot deactivate yourself");
        }

        if (user.getRole().getRole().equals("ROLE_" + Constants.CLUSTER_ADMIN))
            throw ExceptionFactory.customValidationError("The member is an  cluster admin, you cannot perform this operation");

        if (user.getDeactivated()) throw ExceptionFactory.customValidationError("User already deactivated");

        user.setDeactivated(true);
        userRepository.save(user);
    }

    public void activateMember(String memberId, String userId, String userClusterId) {
        User user = userRepository.findById(memberId).orElseThrow(ExceptionFactory::resourceNotFound);
        if (!user.getCluster().getId().equals(userClusterId))
            throw ExceptionFactory.insufficientPermission();

        if (userId.equals(memberId)) {
            throw ExceptionFactory.customValidationError("You cannot activate yourself");
        }
        if (user.getCluster().getDeactivated())
            throw ExceptionFactory.customValidationError("Cluster is deactivated, you cannot activate the member");
        if (user.getRole().getRole().equals("ROLE_" + Constants.CLUSTER_ADMIN))
            throw ExceptionFactory.customValidationError("The member is an  cluster admin, you cannot perform this operation");
        if (!user.getDeactivated()) throw ExceptionFactory.customValidationError("User already activated");
        user.setDeactivated(false);
        userRepository.save(user);
    }

    public void deleteMember(String memberId, String userId, String userClusterId) {
        User user = userRepository.findById(memberId).orElseThrow(ExceptionFactory::resourceNotFound);
        if (!user.getCluster().getId().equals(userClusterId))
            throw ExceptionFactory.insufficientPermission();
        if (userId.equals(memberId)) {
            throw ExceptionFactory.customValidationError("You cannot delete yourself");
        }


        if (user.getRole().getRole().equals("ROLE_" + Constants.CLUSTER_ADMIN))
            throw ExceptionFactory.customValidationError("The member is an  cluster admin, you cannot perform this operation");

          user.setDeleted(true);
          user.getRole().setRole("ROLE_"+Constants.DELETED);

    }

}
