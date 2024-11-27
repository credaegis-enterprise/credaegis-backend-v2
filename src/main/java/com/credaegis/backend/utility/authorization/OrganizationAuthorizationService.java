package com.credaegis.backend.utility.authorization;


import com.credaegis.backend.entity.Cluster;
import com.credaegis.backend.repository.ClusterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrganizationAuthorizationService {
    
    private final ClusterRepository clusterRepository;


    public Boolean isClusterUnderOrganization(String clusterId, String userOrganizationId){
        Cluster cluster = clusterRepository.findById(clusterId).orElseThrow(()->new RuntimeException("new"));
        return cluster.getOrganization().getId().equals(userOrganizationId);
    }
}
