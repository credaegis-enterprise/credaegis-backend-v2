package com.credaegis.backend.service;


import com.credaegis.backend.entity.User;
import com.credaegis.backend.http.request.EventCreationRequest;
import com.credaegis.backend.entity.Cluster;
import com.credaegis.backend.entity.Event;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.repository.ClusterRepository;
import com.credaegis.backend.repository.EventRepository;
import com.credaegis.backend.repository.UserRepository;
import com.github.f4b6a3.ulid.UlidCreator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final ClusterRepository clusterRepository;
    private final UserRepository userRepository;


   public void createEvent(EventCreationRequest eventCreationRequest, User user){
          Cluster cluster = clusterRepository.findById(eventCreationRequest.getClusterId()).orElseThrow(
                  ExceptionFactory::resourceNotFound);
          if(!cluster.getOrganization().getId().equals(user.getOrganization().getId()))
              throw ExceptionFactory.insufficientPermission();


          Event event = new Event();
          event.setId(UlidCreator.getUlid().toString());
          event.setName(eventCreationRequest.getEventName());
          event.setDescription(eventCreationRequest.getDescription());
          event.setCluster(cluster);
          event.setCreatedBy(user);
          eventRepository.save(event);
   }
}
