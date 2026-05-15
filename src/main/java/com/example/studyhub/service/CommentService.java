package com.example.studyhub.service;

import com.example.studyhub.dto.request.AddCommentRequest;
import com.example.studyhub.dto.response.CommentResponse;
import com.example.studyhub.entities.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentService {

    CommentResponse createComment(AddCommentRequest comment);

    List<CommentResponse> getAllComments(int documentId, int page, int pageSize);

    Optional<CommentResponse> getCommentById(Integer id);

    CommentResponse updateComment(Integer id, Comment comment);

    void deleteComment(Integer id);
}