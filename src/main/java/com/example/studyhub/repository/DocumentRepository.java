package com.example.studyhub.repository;

import com.example.studyhub.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Integer> {

}
