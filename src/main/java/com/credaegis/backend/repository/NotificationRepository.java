package com.credaegis.backend.repository;

import com.credaegis.backend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {


    List<Notification> findByTypeAndUser_Id(String type, String userId);
}
