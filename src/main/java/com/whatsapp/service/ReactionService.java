package com.whatsapp.service;

import com.whatsapp.dto.ReactionDto;
import com.whatsapp.model.Reaction;

import java.util.List;
import java.util.Optional;

public interface ReactionService {

    ReactionDto addReaction(Long messageId, Long userId, Reaction.ReactionType reactionType);
    
    void removeReaction(Long messageId, Long userId);
    
    Optional<ReactionDto> getReaction(Long messageId, Long userId);
    
    List<ReactionDto> getMessageReactions(Long messageId);
    
    List<ReactionDto> getReactionsByType(Long messageId, Reaction.ReactionType reactionType);
    
    long getReactionCount(Long messageId, Reaction.ReactionType reactionType);
} 