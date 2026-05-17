package com.example.studyhub.service;

import com.example.studyhub.dto.request.AddDocumentRequest;
import com.example.studyhub.dto.response.DocumentDetailResponse;
import com.example.studyhub.dto.response.DocumentResponse;
import com.example.studyhub.dto.response.DownloadResponse;
import com.example.studyhub.dto.response.PageResult;
import com.example.studyhub.entities.Document;

import java.time.LocalDate;

public interface DocumentService {
    Document create(AddDocumentRequest request);
    PageResult<DocumentResponse> getAll(String search, Integer subjectId,
                                        LocalDate fromDate, LocalDate toDate, int page, int pageSize);
    DocumentDetailResponse getById(Integer id);
    DocumentDetailResponse update(Integer id, Document document);
    void delete(Integer id);
    DownloadResponse download(Integer id);
}