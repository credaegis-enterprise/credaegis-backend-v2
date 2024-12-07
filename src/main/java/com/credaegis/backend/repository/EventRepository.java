package com.credaegis.backend.repository;

import com.credaegis.backend.entity.Cluster;
import com.credaegis.backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface  EventRepository extends JpaRepository<Event,String> {

    @Modifying
    @Query("UPDATE Event e SET e.deactivated = false WHERE e.id = :id")
    void activateEvent(@Param("id") String id);

    @Modifying
    @Query("UPDATE Event e SET e.deactivated = true WHERE e.id = :id")
    void deactivateEvent(@Param("id") String id);

    @Modifying
    @Query("UPDATE Event e SET e.name = :name WHERE e.id = :id")
    void renameEvent(@Param("name") String name, @Param("id") String id);

    @Modifying
    @Query("UPDATE Event e SET e.name = :name, e.description = :description WHERE e.id = :id")
    void updateEvent(@Param("name") String name, @Param("description") String description, @Param("id") String id);


    boolean existsByNameAndCluster(String eventName, Cluster cluster);
}
