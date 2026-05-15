package com.example.studyhub.controller;

import com.example.studyhub.dto.request.AddDocumentRequest;
import com.example.studyhub.dto.response.DocumentDetailResponse;
import com.example.studyhub.dto.response.DocumentResponse;
import com.example.studyhub.entities.Document;
import com.example.studyhub.service.DocumentService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Document create(@ModelAttribute AddDocumentRequest request) {
        return documentService.create(request);
    }

    @GetMapping
    public List<DocumentResponse> getAll(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) int subjectId,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return documentService.getAll(title, description, subjectId, fromDate, toDate, page, pageSize);
    }

    @GetMapping("/{id}")
    public DocumentDetailResponse getById(@PathVariable Integer id) {
        return documentService.getById(id);
    }

    @PutMapping("/{id}")
    public DocumentDetailResponse update(@PathVariable Integer id, @RequestBody Document document) {
        return documentService.update(id, document);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        documentService.delete(id);
    }
}
