package com.credaegis.backend.service;


import com.credaegis.backend.dto.request.MemberCreationRequest;
import com.credaegis.backend.entity.Cluster;
import com.credaegis.backend.entity.User;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.repository.ClusterRepository;
import com.credaegis.backend.repository.OrganizationRepository;
import com.credaegis.backend.repository.UserRepository;
import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Transactional
@Service
@AllArgsConstructor
public class MemberService {

    private final UserRepository userRepository;
    private final ClusterRepository clusterRepository;
    private final OrganizationRepository organizationRepository;


    public void createMember(MemberCreationRequest memberCreationRequest,String userOrganizationId){

        Cluster cluster = clusterRepository.findById(memberCreationRequest.getClusterId()).
                orElseThrow(ExceptionFactory::resourceNotFound);

        if(cluster.getOrganization().getId().equals(userOrganizationId)){
            User user = new User();
            user.setId(UlidCreator.getUlid().toString());
            user.setPassword("sgce");
            user.setUsername(memberCreationRequest.getUsername());
            user.setEmail(memberCreationRequest.getEmail());
            user.setCluster(cluster);
            user.setOrganization(organizationRepository.findById(userOrganizationId).orElseThrow(
                    ExceptionFactory::resourceNotFound
            ));

            userRepository.save(user);

        }
        else throw ExceptionFactory.insufficientPermission();

    }

    public void activateMember(String memberId, String userOrganizationId){

        User user = userRepository.findById(memberId).orElseThrow(ExceptionFactory::resourceNotFound);
        if (!user.getDeactivated()) throw ExceptionFactory.customValidationError("User already activated");
        if (user.getOrganization().getId().equals(userOrganizationId))
            userRepository.activateUser(new ArrayList<>(List.of(memberId)));
        else throw ExceptionFactory.insufficientPermission();

    }

    public void deactivateMember(String memberId, String userOrganizationId){
        User user = userRepository.findById(memberId).orElseThrow(ExceptionFactory::resourceNotFound);
        if (!user.getDeactivated()) throw ExceptionFactory.customValidationError("User already deactivated");
        if (user.getOrganization().getId().equals(userOrganizationId))
            userRepository.deactivateUser(new ArrayList<>(List.of(memberId)));
        else throw ExceptionFactory.insufficientPermission();
    }
}
