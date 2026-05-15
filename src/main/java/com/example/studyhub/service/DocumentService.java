package com.example.studyhub.service;

import com.example.studyhub.dto.request.AddDocumentRequest;
import com.example.studyhub.dto.response.DocumentDetailResponse;
import com.example.studyhub.dto.response.DocumentResponse;
import com.example.studyhub.entities.Document;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;

public interface DocumentService {
    Document create(AddDocumentRequest request);
    List<DocumentResponse> getAll(String title, String description, int subjectId,
                                  LocalDate fromDate, LocalDate toDate, int page, int pageSize);
    DocumentDetailResponse getById(Integer id);
    DocumentDetailResponse update(Integer id, Document document);
    void delete(Integer id);
}