package com.credaegis.backend.service.member;


import com.credaegis.backend.dto.projection.EventInfoProjection;
import com.credaegis.backend.dto.projection.EventSearchProjection;
import com.credaegis.backend.entity.Cluster;
import com.credaegis.backend.entity.Event;
import com.credaegis.backend.entity.User;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.http.request.EventCreationRequest;
import com.credaegis.backend.http.request.EventModificationRequest;
import com.credaegis.backend.repository.ClusterRepository;
import com.credaegis.backend.repository.EventRepository;
import com.credaegis.backend.repository.UserRepository;
import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@AllArgsConstructor
@Service
public class MemberEventService {


    private final EventRepository eventRepository;
    private final ClusterRepository clusterRepository;
    private final UserRepository userRepository;


    //creates an event by organization if cluster and organization are same.
    public void createEvent(EventCreationRequest eventCreationRequest, String userId, String userClusterId) {
        System.out.println(eventCreationRequest.getEventName());
        Cluster cluster = clusterRepository.findById(userClusterId).orElseThrow(
                ExceptionFactory::resourceNotFound);
        User user = userRepository.findById(userId).orElseThrow(
                ExceptionFactory::resourceNotFound
        );
        if (!cluster.getOrganization().getId().equals(user.getOrganization().getId()))
            throw ExceptionFactory.insufficientPermission();

        if (eventRepository.existsByNameAndCluster(eventCreationRequest.getEventName(), cluster))
            throw ExceptionFactory.customValidationError("Event with same name already exists in the cluster");


        Event event = new Event();
        event.setId(UlidCreator.getUlid().toString());
        event.setName(eventCreationRequest.getEventName());
        event.setDescription(eventCreationRequest.getDescription());
        event.setCluster(cluster);
        event.setCreatedBy(user);
        eventRepository.save(event);
    }


    public List<EventInfoProjection> getAllEvents(String userClusterId) {
        return eventRepository.getEvents(userClusterId);
    }

    public List<EventSearchProjection> searchByName(String eventName, String userClusterId) {
        return eventRepository.searchEvents(eventName, userClusterId);

    }


    public void activateEvent(String eventId, String userClusterId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                ExceptionFactory::resourceNotFound
        );
        if (!event.getCluster().getId().equals(userClusterId))
            throw ExceptionFactory.insufficientPermission();
        if (!event.getDeactivated()) throw ExceptionFactory.customValidationError("Event is already active");

        event.setDeactivated(false);
        eventRepository.save(event);


    }

    public void deactivateEvent(String eventId, String userClusterId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                ExceptionFactory::resourceNotFound
        );

        if (!event.getCluster().getId().equals(userClusterId))
            throw ExceptionFactory.insufficientPermission();
        if (event.getDeactivated()) throw ExceptionFactory.customValidationError("Event is already deactivated");

        event.setDeactivated(true);
        eventRepository.save(event);
    }


    public void updateEvent(EventModificationRequest eventModificationRequest, String userClusterId,
                            String eventId){
        Event event = eventRepository.findById(eventId).orElseThrow(
                ExceptionFactory::resourceNotFound
        );

        if(!event.getCluster().getId().equals(userClusterId))
            throw ExceptionFactory.insufficientPermission();

            event.setDescription(eventModificationRequest.getEventDescription());
            event.setName(eventModificationRequest.getEventName());

    }

}
