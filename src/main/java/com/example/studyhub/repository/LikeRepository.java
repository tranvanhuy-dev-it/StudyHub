package com.example.studyhub.repository;

import com.example.studyhub.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    boolean existsByUser_UserIdAndDocument_DocumentId(int userId, int documentId);
    void deleteByUser_UserIdAndDocument_DocumentId(int userId, int documentId);
    int countByDocument_DocumentId(int documentId);
}
