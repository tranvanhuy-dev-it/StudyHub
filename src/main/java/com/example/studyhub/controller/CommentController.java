package com.example.studyhub.controller;

import com.example.studyhub.dto.request.AddCommentRequest;
import com.example.studyhub.dto.response.CommentResponse;
import com.example.studyhub.entities.Comment;
import com.example.studyhub.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@RequestBody AddCommentRequest comment) {
        return ResponseEntity.ok(commentService.createComment(comment));
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getAllComments(
            @RequestParam(required = true) int documentId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        return ResponseEntity.ok(commentService.getAllComments(documentId, page, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable Integer id) {
        return commentService.getCommentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Integer id,
            @RequestBody Comment comment) {
        return ResponseEntity.ok(commentService.updateComment(id, comment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Integer id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok("Delete comment successfully");
    }
}