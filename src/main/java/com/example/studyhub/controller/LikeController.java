package com.example.studyhub.controller;

import com.example.studyhub.annotation.CurrentUserId;
import com.example.studyhub.dto.response.LikeResponse;
import com.example.studyhub.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

@RestController
@RequestMapping("/api/likes")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/{documentId}")
    public ResponseEntity<LikeResponse> toggleLike(
            @CurrentUserId Integer userId,
            @PathVariable int documentId
    ) {
        LikeResponse like = likeService.toggleLike(userId, documentId);
        return ResponseEntity.ok(like);
    }

    @GetMapping("/status/{documentId}")
    public ResponseEntity<Boolean> isLiked(
            @PathVariable int documentId,
            @CurrentUserId Integer userId) {

        if (userId == null) {
            return ResponseEntity.ok(false);
        }

        boolean isLiked = likeService.isLike(userId, documentId);
        return ResponseEntity.ok(isLiked);
    }
}
