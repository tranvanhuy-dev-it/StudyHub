package com.example.studyhub.controller;

import com.example.studyhub.annotation.CurrentUserId;
import com.example.studyhub.dto.request.AddCommentRequest;
import com.example.studyhub.dto.request.UpdateCommentRequest;
import com.example.studyhub.dto.response.CommentResponse;
import com.example.studyhub.dto.response.PageResult;
import com.example.studyhub.entities.Comment;
import com.example.studyhub.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @CurrentUserId Integer userId,
            @RequestBody AddCommentRequest comment
    ) {
        return ResponseEntity.ok(commentService.createComment(userId, comment.getDocumentId(), comment.getContent()));
    }

    @GetMapping("/document/{documentId}")
    public ResponseEntity<PageResult<CommentResponse>> getAllComments(
            @PathVariable  int documentId,
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
            @RequestBody UpdateCommentRequest request) {
        return ResponseEntity.ok(commentService.updateComment(id, request.getContent()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Integer id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok("Delete comment successfully");
    }
}