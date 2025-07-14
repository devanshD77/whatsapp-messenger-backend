package com.whatsapp.repository;

import com.whatsapp.model.Message;
import com.whatsapp.model.Reaction;
import com.whatsapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    List<Reaction> findByMessage(Message message);
    
    @Query("SELECT r FROM Reaction r WHERE r.message = :message AND r.user = :user")
    Optional<Reaction> findByMessageAndUser(@Param("message") Message message, @Param("user") User user);
    
    @Query("SELECT COUNT(r) FROM Reaction r WHERE r.message = :message AND r.user = :user")
    long countByMessageAndUser(@Param("message") Message message, @Param("user") User user);
    
    @Query("SELECT r FROM Reaction r WHERE r.message = :message AND r.reactionType = :reactionType")
    List<Reaction> findByMessageAndReactionType(@Param("message") Message message, 
                                               @Param("reactionType") Reaction.ReactionType reactionType);
    
    void deleteByMessageAndUser(Message message, User user);
} 