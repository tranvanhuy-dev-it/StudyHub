package com.example.studyhub.service.impl;

import com.example.studyhub.dto.request.AddCommentRequest;
import com.example.studyhub.dto.response.CommentResponse;
import com.example.studyhub.dto.response.PageResult;
import com.example.studyhub.entities.Comment;
import com.example.studyhub.entities.Document;
import com.example.studyhub.entities.User;
import com.example.studyhub.repository.CommentRepository;
import com.example.studyhub.repository.DocumentRepository;
import com.example.studyhub.repository.UserRepository;
import com.example.studyhub.service.CommentService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public CommentResponse createComment(int userId, int documentId, String content) {

        Document doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setDocument(doc);
        comment.setUser(user);

        Comment saved =  commentRepository.save(comment);

        return mapToCommentResponse(saved);
    }

    @Override
    public PageResult<CommentResponse> getAllComments(int documentId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);

        Page<Comment> listComments = commentRepository
                .findByDocument_DocumentId(documentId, pageable);

        List<CommentResponse> comments = listComments.stream()
                .map(this::mapToCommentResponse)
                .toList();

        long total = commentRepository.countByDocument_DocumentId(documentId);

        int totalPages = (int) Math.ceil((double) total / pageSize);

        return new PageResult<>(comments, total, totalPages, page, pageSize);
    }

    @Override
    public Optional<CommentResponse> getCommentById(Integer id) {

        Comment cmt = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        return Optional.of(mapToCommentResponse(cmt));
    }

    @Override
    public CommentResponse updateComment(Integer id, String comment) {
        return commentRepository.findById(id)
                .map(existing -> {
                    existing.setContent(comment);
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