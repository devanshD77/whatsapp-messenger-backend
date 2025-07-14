package com.whatsapp.repository;

import com.whatsapp.model.Chatroom;
import com.whatsapp.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findByChatroomOrderByCreatedAtDesc(Chatroom chatroom, Pageable pageable);
    
    @Query("SELECT m FROM Message m WHERE m.chatroom = :chatroom ORDER BY m.createdAt DESC")
    Page<Message> findMessagesByChatroom(@Param("chatroom") Chatroom chatroom, Pageable pageable);
    
    @Query("SELECT COUNT(m) FROM Message m WHERE m.chatroom = :chatroom")
    long countByChatroom(@Param("chatroom") Chatroom chatroom);
    
    @Query("SELECT m FROM Message m WHERE m.chatroom = :chatroom AND m.id < :messageId ORDER BY m.createdAt DESC")
    Page<Message> findMessagesBeforeId(@Param("chatroom") Chatroom chatroom, 
                                      @Param("messageId") Long messageId, 
                                      Pageable pageable);
    
    List<Message> findByChatroomOrderByCreatedAtAsc(Chatroom chatroom);
    
    @Query("SELECT m FROM Message m WHERE m.chatroom = :chatroom AND m.content LIKE %:searchTerm% ORDER BY m.createdAt DESC")
    List<Message> findByChatroomAndContentContainingIgnoreCase(@Param("chatroom") Chatroom chatroom, 
                                                             @Param("searchTerm") String searchTerm);
} 