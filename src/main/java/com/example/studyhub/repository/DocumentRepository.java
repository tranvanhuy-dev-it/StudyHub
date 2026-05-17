package com.example.studyhub.repository;

import com.example.studyhub.entities.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.print.Doc;
import java.time.LocalDate;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Integer> {
    @Query("""
    SELECT d FROM Document d
    WHERE (:title IS NULL OR LOWER(d.title) LIKE LOWER(CONCAT('%', :title, '%')))
    AND (:description IS NULL OR LOWER(d.description) LIKE LOWER(CONCAT('%', :description, '%')))
    AND (:subjectId = 0 OR d.subject.subjectId = :subjectId)
    AND (:fromDate IS NULL OR d.createdAt >= :fromDate)
    AND (:toDate IS NULL OR d.createdAt <= :toDate)
    """)
    Page<Document> findByFilters(
            @Param("title") String title,
            @Param("description") String description,
            @Param("subjectId") Integer subjectId,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            Pageable pageable
    );

    @Modifying
    @Query("UPDATE Document d SET d.viewCount = d.viewCount + 1 WHERE d.id = :id")
    int incrementViewCount(@Param("id") Integer id);
}
