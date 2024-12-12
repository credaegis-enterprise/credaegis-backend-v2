package com.credaegis.backend.service;


import com.credaegis.backend.entity.Organization;
import com.credaegis.backend.entity.User;
import com.credaegis.backend.http.request.EventCreationRequest;
import com.credaegis.backend.entity.Cluster;
import com.credaegis.backend.entity.Event;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.http.request.EventModificationRequest;
import com.credaegis.backend.repository.ClusterRepository;
import com.credaegis.backend.repository.EventRepository;
import com.credaegis.backend.repository.UserRepository;
import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final ClusterRepository clusterRepository;
    private final UserRepository userRepository;


    //creates an event by organization if cluster and organization are same.
   public void createEvent(EventCreationRequest eventCreationRequest, String userId){
          Cluster cluster = clusterRepository.findById(eventCreationRequest.getClusterId()).orElseThrow(
                  ExceptionFactory::resourceNotFound);
          User user = userRepository.findById(userId).orElseThrow(
                  ExceptionFactory::resourceNotFound
          );
          if(!cluster.getOrganization().getId().equals(user.getOrganization().getId()))
              throw ExceptionFactory.insufficientPermission();

          if(eventRepository.existsByNameAndCluster(eventCreationRequest.getEventName(),cluster))
              throw ExceptionFactory.customValidationError("Event with same name already exists in the cluster");


      
          Event event = new Event();
          event.setId(UlidCreator.getUlid().toString());
          event.setName(eventCreationRequest.getEventName());
          event.setDescription(eventCreationRequest.getDescription());
          event.setCluster(cluster);
          event.setCreatedBy(user);
          eventRepository.save(event);
   }

   public void activateEvent(String eventId, String userOrganizationId){
       Event event = eventRepository.findById(eventId).orElseThrow(
               ExceptionFactory::resourceNotFound
       );
       if(!event.getCluster().getOrganization().getId().equals(userOrganizationId))
           throw ExceptionFactory.insufficientPermission();
       if(!event.getDeactivated()) throw ExceptionFactory.customValidationError("Event is already active");

       eventRepository.activateEvent(eventId);
   }

   public void deactivateEvent(String eventId, String userOrganizationId){
       Event event = eventRepository.findById(eventId).orElseThrow(
               ExceptionFactory::resourceNotFound
       );

       if(!event.getCluster().getOrganization().getId().equals(userOrganizationId))
           throw ExceptionFactory.insufficientPermission();
       if(event.getDeactivated()) throw ExceptionFactory.customValidationError("Event is already deactivated");
       eventRepository.deactivateEvent(eventId);
   }

   public void updateEvent(EventModificationRequest eventModificationRequest, String userOrganizationId,
                           String eventId){
       Event event = eventRepository.findById(eventId).orElseThrow(
               ExceptionFactory::resourceNotFound
       );

       if(!event.getCluster().getOrganization().getId().equals(userOrganizationId))
           throw ExceptionFactory.insufficientPermission();

       eventRepository.updateEvent(eventModificationRequest.getEventName(),
               eventModificationRequest.getEventDescription(),eventId);

   }
}
