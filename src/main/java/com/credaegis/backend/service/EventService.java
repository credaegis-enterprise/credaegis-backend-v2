package com.credaegis.backend.service;


import com.credaegis.backend.dto.request.EventCreationRequest;
import com.credaegis.backend.entity.Cluster;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.repository.ClusterRepository;
import com.credaegis.backend.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final ClusterRepository clusterRepository;


    public void CreateEvent(EventCreationRequest eventCreationRequest,String clusterId,
                            String userOrganizationId){

        Cluster cluster = clusterRepository.findById(clusterId).orElseThrow(ExceptionFactory::resourceNotFound);
        if(cluster.getOrganization().getId().equals(userOrganizationId)){

        }
        else throw ExceptionFactory.insufficientPermission();

    }
}
