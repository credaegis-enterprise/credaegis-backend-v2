package com.credaegis.backend.repository;

import com.credaegis.backend.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventsRepository extends JpaRepository<Events,String> {
}
