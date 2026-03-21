package com.carrental.server.repository;

import com.carrental.server.model.EmailNotification;
import com.carrental.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailNotificationRepository extends JpaRepository<EmailNotification, Long> {

    List<EmailNotification> findBySender(User sender);

    List<EmailNotification> findByReceiver(User receiver);

    @Query("SELECT e FROM EmailNotification e WHERE e.receiver.id = :receiverId ORDER BY e.sentAt DESC")
    List<EmailNotification> findByReceiverIdOrderBySentAtDesc(@Param("receiverId") Long receiverId);

    @Query("SELECT e FROM EmailNotification e WHERE e.sender.id = :senderId ORDER BY e.sentAt DESC")
    List<EmailNotification> findBySenderIdOrderBySentAtDesc(@Param("senderId") Long senderId);
}