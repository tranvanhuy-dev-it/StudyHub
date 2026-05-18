package com.example.studyhub.controller;

import com.example.studyhub.annotation.CurrentUserId;
import com.example.studyhub.dto.request.AddDocumentRequest;
import com.example.studyhub.dto.response.*;
import com.example.studyhub.entities.Document;
import com.example.studyhub.repository.DocumentRepository;
import com.example.studyhub.service.BookmarkService;
import com.example.studyhub.service.DocumentService;
import com.example.studyhub.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;
    private final LikeService likeService;
    private final BookmarkService bookmarkService;

    public DocumentController(DocumentService documentService, LikeService likeService, BookmarkService bookmarkService) {
        this.documentService = documentService;
        this.likeService = likeService;
        this.bookmarkService = bookmarkService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Document> create(@ModelAttribute AddDocumentRequest request) {
        Document created = documentService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }


    @GetMapping
    public ResponseEntity<PageResult<DocumentResponse>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer subjectId,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        PageResult<DocumentResponse> documents = documentService.getAll(search, subjectId, fromDate, toDate, page, pageSize);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDetailResponse> getById(@PathVariable Integer id) {
        try {
            DocumentDetailResponse document = documentService.getById(id);
            return ResponseEntity.ok(document);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<DocumentDetailResponse> update(@PathVariable Integer id, @RequestBody Document document) {
        try {
            DocumentDetailResponse updated = documentService.update(id, document);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            documentService.delete(id);
            return ResponseEntity.noContent().build();  // 204 NO CONTENT
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Integer id) {
        DownloadResponse result = documentService.download(id);

        MediaType mediaType;
        try {
            mediaType = MediaType.parseMediaType(result.getContentType());
        } catch (Exception e) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + result.getFilename() + "\"")
                .contentType(mediaType)
                .body(result.getResource());
    }
}