package com.example.studyhub.service.impl;

import com.example.studyhub.dto.request.AddCommentRequest;
import com.example.studyhub.dto.response.CommentResponse;
import com.example.studyhub.entities.Comment;
import com.example.studyhub.entities.Document;
import com.example.studyhub.entities.User;
import com.example.studyhub.repository.CommentRepository;
import com.example.studyhub.repository.DocumentRepository;
import com.example.studyhub.repository.UserRepository;
import com.example.studyhub.service.CommentService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository,
                              DocumentRepository documentRepository,
                              UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public CommentResponse createComment(AddCommentRequest request) {

        Document doc = documentRepository.findById(request.getDocumentId())
                .orElseThrow(() -> new RuntimeException("Document not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setDocument(doc);
        comment.setUser(user);

        Comment saved =  commentRepository.save(comment);

        return mapToCommentResponse(saved);
    }

    @Override
    public List<CommentResponse> getAllComments(int documentId, int page, int pageSize) {
        List<Comment> cmts = commentRepository.findAll();

        return cmts.stream()
                .filter(comment -> comment.getDocument().getDocumentId() == documentId)
                .skip((long) page * pageSize)
                .limit(pageSize)
                .map(this::mapToCommentResponse)
                .toList();
    }

    @Override
    public Optional<CommentResponse> getCommentById(Integer id) {

        Comment cmt = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        return Optional.of(mapToCommentResponse(cmt));
    }

    @Override
    public CommentResponse updateComment(Integer id, Comment comment) {
        return commentRepository.findById(id)
                .map(existing -> {
                    existing.setContent(comment.getContent());
                    existing.setCreatedAt(comment.getCreatedAt());
                    existing.setDocument(comment.getDocument());
                    existing.setUser(comment.getUser());
                    return mapToCommentResponse(commentRepository.save(existing));
                })
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
    }

    @Override
    public void deleteComment(Integer id) {
        commentRepository.deleteById(id);
    }

    private CommentResponse mapToCommentResponse(Comment cmt) {
        return new CommentResponse(
                cmt.getCommentId(),
                cmt.getContent(),
                cmt.getCreatedAt(),
                cmt.getUser().getUserId(),
                cmt.getUser().getFullName()
        );
    }
}