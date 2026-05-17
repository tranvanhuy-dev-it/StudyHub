package com.example.studyhub.repository;

import com.example.studyhub.entities.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;


import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
    Page<Bookmark> findByUser_UserId(Integer userId, Pageable pageable);
    int countByDocument_DocumentId(int documentId);
    boolean existsByUser_UserIdAndDocument_DocumentId(int userId, int documentId);
    Optional<Bookmark> findByUser_UserIdAndDocument_DocumentId(int userId, int documentId);
    long countByUser_UserId(int userId);
    void deleteByUser_UserIdAndDocument_DocumentId(Integer userId, Integer documentId);
}
