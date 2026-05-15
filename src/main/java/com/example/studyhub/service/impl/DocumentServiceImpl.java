package com.example.studyhub.service.impl;

import com.example.studyhub.dto.request.AddDocumentRequest;
import com.example.studyhub.dto.response.CommentResponse;
import com.example.studyhub.dto.response.DocumentDetailResponse;
import com.example.studyhub.dto.response.DocumentResponse;
import com.example.studyhub.entities.Document;
import com.example.studyhub.entities.Subject;
import com.example.studyhub.entities.Tag;
import com.example.studyhub.entities.User;
import com.example.studyhub.repository.DocumentRepository;
import com.example.studyhub.repository.SubjectRepository;
import com.example.studyhub.repository.TagRepository;
import com.example.studyhub.repository.UserRepository;
import com.example.studyhub.service.DocumentService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository,
                               TagRepository tagRepository,
                               UserRepository userRepository,
                               SubjectRepository subjectRepository) {
        this.documentRepository = documentRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public Document create(AddDocumentRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Set<Tag> tags = new HashSet<>();

        if (request.getTagIds() != null) {
            tags = new HashSet<>(
                    tagRepository.findAllById(request.getTagIds())
            );
        }

        MultipartFile file = request.getFile();

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String uploadDir = "uploads/";

        Path filePath;

        try {
            Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads");
            Files.createDirectories(uploadPath);

            filePath = uploadPath.resolve(fileName);
            Files.write(filePath, file.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("File upload failed: " + e.getMessage());
        }

        Document doc = new Document();
        doc.setTitle(request.getTitle());
        doc.setDescription(request.getDescription());

        doc.setFileUrl(filePath.toString());

        doc.setFileType(file.getContentType());
        doc.setFileSize(file.getSize());

        doc.setThumbnailUrl(request.getThumbnailUrl());
        doc.setVisibility(request.getVisibility());

        doc.setCreatedAt(LocalDateTime.now());

        doc.setUser(user);
        doc.setSubject(subject);
        doc.setTags(tags);

        doc.setStatus("PENDING");
        doc.setDownloadCount(0);
        doc.setViewCount(0);

        return documentRepository.save(doc);
    }

    @Override
    public List<DocumentResponse> getAll(String title, String description, int subjectId,
                                         LocalDate fromDate, LocalDate toDate, int page, int pagesize) {
        return documentRepository.findAll()
                .stream()
                .filter(doc -> title == null || doc.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(doc -> description == null || doc.getDescription().toLowerCase().contains(description.toLowerCase()))
                .filter(doc -> subjectId == 0 || doc.getSubject().getSubjectId() == subjectId)
                .filter(doc -> fromDate == null || !doc.getCreatedAt().toLocalDate().isBefore(fromDate))
                .filter(doc -> toDate == null || !doc.getCreatedAt().toLocalDate().isAfter(toDate))
                .skip((long) (page - 1) * pagesize)
                .limit(pagesize)
                .map(this::mapToDocumentResponse)
                .toList();
    }

    @Transactional
    @Override
    public DocumentDetailResponse getById(Integer id) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        return mapToDocumentDetailResponse(doc);
    }

    @Override
    public DocumentDetailResponse update(Integer id, Document document) {
        Document existing = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        existing.setTitle(document.getTitle());
        existing.setDescription(document.getDescription());
        existing.setFileUrl(document.getFileUrl());
        existing.setFileType(document.getFileType());
        existing.setFileSize(document.getFileSize());
        existing.setThumbnailUrl(document.getThumbnailUrl());
        existing.setStatus(document.getStatus());
        existing.setVisibility(document.getVisibility());
        existing.setSubject(document.getSubject());
        existing.setTags(document.getTags());

        Document doc = documentRepository.save(existing);

        return mapToDocumentDetailResponse(doc);
    }

    @Override
    public void delete(Integer id) {
        documentRepository.deleteById(id);
    }

    private DocumentDetailResponse mapToDocumentDetailResponse(Document doc) {
        return new DocumentDetailResponse(
                doc.getDocumentId(),
                doc.getTitle(),
                doc.getDescription(),
                doc.getFileUrl(),
                doc.getFileType(),
                doc.getFileSize(),
                doc.getThumbnailUrl(),
                doc.getDownloadCount(),
                doc.getViewCount(),
                doc.getStatus(),
                doc.getVisibility(),
                doc.getCreatedAt(),
                doc.getLikes().size(),
                doc.getUser().getUserId(),
                doc.getUser().getFullName(),
                doc.getSubject().getSubjectId(),
                doc.getSubject().getSubjectName(),
                doc.getUser().getSchool().getSchoolId(),
                doc.getUser().getSchool().getSchoolName()
        );
    }

    private DocumentResponse mapToDocumentResponse(Document doc) {
        return new DocumentResponse(
                doc.getDocumentId(),
                doc.getTitle(),
                doc.getFileUrl(),
                doc.getFileType(),
                doc.getFileSize(),
                doc.getCreatedAt(),
                doc.getUser().getSchool().getSchoolName()
        );
    }
}