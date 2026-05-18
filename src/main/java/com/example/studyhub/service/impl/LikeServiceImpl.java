package com.example.studyhub.service.impl;

import com.example.studyhub.dto.request.AddLikeRequest;
import com.example.studyhub.dto.response.LikeResponse;
import com.example.studyhub.entities.Document;
import com.example.studyhub.entities.Like;
import com.example.studyhub.entities.User;
import com.example.studyhub.repository.DocumentRepository;
import com.example.studyhub.repository.LikeRepository;
import com.example.studyhub.repository.UserRepository;
import com.example.studyhub.service.LikeService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    public LikeServiceImpl(LikeRepository likeRepository,
                           DocumentRepository documentRepository, UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public LikeResponse toggleLike(int userId, int documentId) {
        boolean isLiked = likeRepository.existsByUser_UserIdAndDocument_DocumentId(userId, documentId);

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        if (isLiked) {
            likeRepository.deleteByUser_UserIdAndDocument_DocumentId(userId, documentId);
            document.setLikeCount(document.getLikeCount() - 1);
            documentRepository.save(document);

            return new LikeResponse(false, document.getLikeCount());
        } else {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Like like = new Like();
            like.setCreatedAt(LocalDateTime.now());
            like.setDocument(document);
            like.setUser(user);
            likeRepository.save(like);

            document.setLikeCount(document.getLikeCount() + 1);
            documentRepository.save(document);

            return new LikeResponse(true, document.getLikeCount());
        }
    }

    @Override
    public boolean isLike(int userId, int documentId) {
        return likeRepository.existsByUser_UserIdAndDocument_DocumentId(userId, documentId);
    }

    @Override
    public int getCountByDocumentId(int documentId) {
        return likeRepository.countByDocument_DocumentId(documentId);
    }
}