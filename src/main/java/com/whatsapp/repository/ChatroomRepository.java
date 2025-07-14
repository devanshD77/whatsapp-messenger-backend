package com.whatsapp.repository;

import com.whatsapp.model.Chatroom;
import com.whatsapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {

    @Query("SELECT c FROM Chatroom c WHERE " +
           "(c.user1 = :user1 AND c.user2 = :user2) OR " +
           "(c.user1 = :user2 AND c.user2 = :user1)")
    Optional<Chatroom> findByUsers(@Param("user1") User user1, @Param("user2") User user2);

    @Query("SELECT c FROM Chatroom c WHERE " +
           "c.user1 = :user OR c.user2 = :user " +
           "ORDER BY c.updatedAt DESC")
    Page<Chatroom> findByUserOrderByUpdatedAtDesc(@Param("user") User user, Pageable pageable);

    @Query("SELECT c FROM Chatroom c WHERE " +
           "c.user1 = :user OR c.user2 = :user")
    List<Chatroom> findAllByUser(@Param("user") User user);

    @Query("SELECT COUNT(c) FROM Chatroom c WHERE " +
           "(c.user1 = :user1 AND c.user2 = :user2) OR " +
           "(c.user1 = :user2 AND c.user2 = :user1)")
    long countByUsers(@Param("user1") User user1, @Param("user2") User user2);
} 