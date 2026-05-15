package com.example.studyhub.service.impl;

import com.example.studyhub.dto.request.AddLikeRequest;
import com.example.studyhub.entities.Document;
import com.example.studyhub.entities.Like;
import com.example.studyhub.entities.User;
import com.example.studyhub.repository.DocumentRepository;
import com.example.studyhub.repository.LikeRepository;
import com.example.studyhub.repository.UserRepository;
import com.example.studyhub.service.LikeService;
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

    @Override
    public Like create(AddLikeRequest request) {
        Document doc = documentRepository.findById(request.getDocumentId())
                .orElseThrow(() -> new RuntimeException("Document not found"));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Like like = new Like();
        like.setCreatedAt(LocalDateTime.now());
        like.setDocument(doc);
        like.setUser(user);
        return likeRepository.save(like);
    }

    @Override
    public List<Like> getAll() {
        return likeRepository.findAll();
    }

    @Override
    public void delete(Integer id) {
        likeRepository.deleteById(id);
    }
}