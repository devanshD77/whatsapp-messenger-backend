package com.whatsapp.controller;

import com.whatsapp.dto.ReactionDto;
import com.whatsapp.model.Reaction;
import com.whatsapp.service.ReactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reactions")
@Tag(name = "Reaction Management", description = "APIs for managing emoji reactions on messages")
public class ReactionController {

    @Autowired
    private ReactionService reactionService;

    @PostMapping
    @Operation(summary = "Add reaction", description = "Adds an emoji reaction to a message")
    public ResponseEntity<ReactionDto> addReaction(
            @Parameter(description = "Message ID") @RequestParam Long messageId,
            @Parameter(description = "User ID") @RequestParam Long userId,
            @Parameter(description = "Reaction type") @RequestParam Reaction.ReactionType reactionType) {
        ReactionDto reaction = reactionService.addReaction(messageId, userId, reactionType);
        return ResponseEntity.status(HttpStatus.CREATED).body(reaction);
    }

    @DeleteMapping
    @Operation(summary = "Remove reaction", description = "Removes a user's reaction from a message")
    public ResponseEntity<Void> removeReaction(
            @Parameter(description = "Message ID") @RequestParam Long messageId,
            @Parameter(description = "User ID") @RequestParam Long userId) {
        reactionService.removeReaction(messageId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/message/{messageId}")
    @Operation(summary = "Get message reactions", description = "Retrieves all reactions for a specific message")
    public ResponseEntity<List<ReactionDto>> getMessageReactions(
            @Parameter(description = "Message ID") @PathVariable Long messageId) {
        List<ReactionDto> reactions = reactionService.getMessageReactions(messageId);
        return ResponseEntity.ok(reactions);
    }

    @GetMapping("/message/{messageId}/type/{reactionType}")
    @Operation(summary = "Get reactions by type", description = "Retrieves reactions of a specific type for a message")
    public ResponseEntity<List<ReactionDto>> getReactionsByType(
            @Parameter(description = "Message ID") @PathVariable Long messageId,
            @Parameter(description = "Reaction type") @PathVariable Reaction.ReactionType reactionType) {
        List<ReactionDto> reactions = reactionService.getReactionsByType(messageId, reactionType);
        return ResponseEntity.ok(reactions);
    }

    @GetMapping("/message/{messageId}/user/{userId}")
    @Operation(summary = "Get user reaction", description = "Retrieves a specific user's reaction on a message")
    public ResponseEntity<ReactionDto> getUserReaction(
            @Parameter(description = "Message ID") @PathVariable Long messageId,
            @Parameter(description = "User ID") @PathVariable Long userId) {
        return reactionService.getReaction(messageId, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/message/{messageId}/type/{reactionType}/count")
    @Operation(summary = "Get reaction count", description = "Retrieves the count of reactions of a specific type")
    public ResponseEntity<Long> getReactionCount(
            @Parameter(description = "Message ID") @PathVariable Long messageId,
            @Parameter(description = "Reaction type") @PathVariable Reaction.ReactionType reactionType) {
        long count = reactionService.getReactionCount(messageId, reactionType);
        return ResponseEntity.ok(count);
    }
} 