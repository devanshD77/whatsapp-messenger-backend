package com.whatsapp.service.impl;

import com.whatsapp.dto.ReactionDto;
import com.whatsapp.dto.UserDto;
import com.whatsapp.model.Message;
import com.whatsapp.model.Reaction;
import com.whatsapp.model.User;
import com.whatsapp.repository.MessageRepository;
import com.whatsapp.repository.ReactionRepository;
import com.whatsapp.repository.UserRepository;
import com.whatsapp.service.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReactionServiceImpl implements ReactionService {

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ReactionDto addReaction(Long messageId, Long userId, Reaction.ReactionType reactionType) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + messageId));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Check if user already has a reaction on this message
        Optional<Reaction> existingReaction = reactionRepository.findByMessageAndUser(message, user);
        if (existingReaction.isPresent()) {
            // Update existing reaction
            Reaction reaction = existingReaction.get();
            reaction.setReactionType(reactionType);
            Reaction updatedReaction = reactionRepository.save(reaction);
            return convertToDto(updatedReaction);
        }

        // Create new reaction
        Reaction reaction = new Reaction(reactionType, user, message);
        Reaction savedReaction = reactionRepository.save(reaction);
        return convertToDto(savedReaction);
    }

    @Override
    public void removeReaction(Long messageId, Long userId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + messageId));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        reactionRepository.deleteByMessageAndUser(message, user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReactionDto> getReaction(Long messageId, Long userId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + messageId));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        return reactionRepository.findByMessageAndUser(message, user)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReactionDto> getMessageReactions(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + messageId));

        return reactionRepository.findByMessage(message)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReactionDto> getReactionsByType(Long messageId, Reaction.ReactionType reactionType) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + messageId));

        return reactionRepository.findByMessageAndReactionType(message, reactionType)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long getReactionCount(Long messageId, Reaction.ReactionType reactionType) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + messageId));

        return reactionRepository.findByMessageAndReactionType(message, reactionType).size();
    }

    private ReactionDto convertToDto(Reaction reaction) {
        UserDto userDto = convertUserToDto(reaction.getUser());
        
        return new ReactionDto(
                reaction.getId(),
                reaction.getReactionType(),
                userDto,
                reaction.getMessage().getId(),
                reaction.getCreatedAt()
        );
    }

    private UserDto convertUserToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getBio(),
                user.getAvatarUrl(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
} 