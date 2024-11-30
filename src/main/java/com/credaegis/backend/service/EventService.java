package com.credaegis.backend.service;


import com.credaegis.backend.http.request.EventCreationRequest;
import com.credaegis.backend.entity.Cluster;
import com.credaegis.backend.entity.Event;
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


    public void createEvent(EventCreationRequest eventCreationRequest,String clusterId,
                            String userOrganizationId){

        Cluster cluster = clusterRepository.findById(clusterId).orElseThrow(ExceptionFactory::resourceNotFound);
        if(cluster.getOrganization().getId().equals(userOrganizationId)){
                        System.out.println("yet");
        }
        else throw ExceptionFactory.insufficientPermission();

    }


    public void activateEvent(String eventId,String userOrganizationId){

        Event event = eventRepository.findById(eventId).orElseThrow(ExceptionFactory::resourceNotFound);
        if(!event.getDeactivated()) throw ExceptionFactory.customValidationError("Event already Activated");
        if(event.getCluster().getOrganization().getId().equals(userOrganizationId))
            eventRepository.activateEvent(eventId);
        else throw ExceptionFactory.insufficientPermission();

    }

    public void deactivateEvent(String eventId,String userOrganizationId){
        Event event = eventRepository.findById(eventId).orElseThrow(ExceptionFactory::resourceNotFound);
        if(!event.getDeactivated()) throw ExceptionFactory.customValidationError("Event already Deactivated");
        if(event.getCluster().getOrganization().getId().equals(userOrganizationId))
            eventRepository.deactivateEvent(eventId);
        else throw ExceptionFactory.insufficientPermission();
    }

    public void renameEvent(String eventId,String newName, String userOrganizationId){
        Event event = eventRepository.findById(eventId).orElseThrow(ExceptionFactory::resourceNotFound);
        if(event.getCluster().getOrganization().getId().equals(userOrganizationId))
            eventRepository.renameEvent(newName, eventId);
        else throw ExceptionFactory.insufficientPermission();
    }
}
