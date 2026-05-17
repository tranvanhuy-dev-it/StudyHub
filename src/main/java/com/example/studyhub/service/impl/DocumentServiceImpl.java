package com.example.studyhub.service.impl;

import com.example.studyhub.dto.request.AddDocumentRequest;
import com.example.studyhub.dto.response.DocumentDetailResponse;
import com.example.studyhub.dto.response.DocumentResponse;
import com.example.studyhub.dto.response.DownloadResponse;
import com.example.studyhub.dto.response.PageResult;
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
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
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

    @Transactional
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

        String originalFilename = file.getOriginalFilename();
        String cleanFilename = sanitizeFilename(originalFilename);
        String fileName = System.currentTimeMillis() + "_" + cleanFilename;

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

        doc.setFileUrl(fileName);

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
        doc.setLikeCount(0);
        doc.setBookmarkCount(0);

        Document savedDoc = documentRepository.save(doc);

        try {
            generateThumbnail(savedDoc);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to generate thumbnail: " + e.getMessage());
        }

        return documentRepository.save(doc);
    }

    private String sanitizeFilename(String filename) {
        if (filename == null) return null;

        return filename
                .replaceAll("[\\[\\]<>\"|?*:/\\\\]", "_")
                .replaceAll("\\s+", "_")
                .replaceAll("[^a-zA-Z0-9._-]", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_|_$", "");
    }

    @Override
    public PageResult<DocumentResponse> getAll(
            String search,
            Integer subjectId,
            LocalDate fromDate,
            LocalDate toDate,
            int page,
            int pageSize
    ) {

        List<Document> documentsList = documentRepository.findAll();

        List<DocumentResponse> documents = documentsList.stream()
                .filter(doc -> search == null || search.isBlank() ||
                        doc.getTitle().toLowerCase().contains(search.toLowerCase()) ||
                        doc.getDescription().toLowerCase().contains(search.toLowerCase()) ||
                        doc.getSubject().getSubjectName().toLowerCase().contains(search.toLowerCase()) ||
                        doc.getSubject().getSchool().getSchoolName().toLowerCase().contains(search.toLowerCase())
                )

                .filter(doc -> subjectId == null ||
                        doc.getSubject().getSubjectId().equals(subjectId))

                .filter(doc -> fromDate == null ||
                        !doc.getCreatedAt().toLocalDate().isBefore(fromDate))

                .filter(doc -> toDate == null ||
                        !doc.getCreatedAt().toLocalDate().isAfter(toDate))

                .sorted(Comparator.comparing(Document::getCreatedAt).reversed())

                .map(this::mapToDocumentResponse)
                .toList();

        // pagination bằng stream (manual)
        int total = documents.size();
        int totalPages = (int) Math.ceil((double) total / pageSize);

        List<DocumentResponse> paged = documents.stream()
                .skip((long) (page - 1) * pageSize)
                .limit(pageSize)
                .toList();

        return new PageResult<>(
                paged,
                (long) total,
                totalPages,
                page,
                pageSize
        );
    }

    @Transactional
    @Override
    public DocumentDetailResponse getById(Integer id) {
        documentRepository.incrementViewCount(id);

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
        String webUrl = "/api/files/" + doc.getFileUrl();

        return new DocumentDetailResponse(
                doc.getDocumentId(),
                doc.getTitle(),
                doc.getDescription(),
                webUrl,
                doc.getFileType(),
                doc.getFileSize(),
                doc.getThumbnailUrl(),
                doc.getDownloadCount(),
                doc.getViewCount(),
                doc.getStatus(),
                doc.getVisibility(),
                doc.getCreatedAt(),
                doc.getLikeCount(),
                doc.getBookmarkCount(),
                doc.getUser().getUserId(),
                doc.getUser().getFullName(),
                doc.getSubject().getSubjectId(),
                doc.getSubject().getSubjectName(),
                doc.getUser().getSchool().getSchoolId(),
                doc.getUser().getSchool().getSchoolName()
        );
    }

    @Transactional
    @Override
    public DownloadResponse download(Integer id) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads");
        Path filePath = uploadPath.resolve(doc.getFileUrl());

        if (!Files.exists(filePath)) {
            throw new RuntimeException("File not found on server: " + doc.getFileUrl());
        }

        doc.setDownloadCount(doc.getDownloadCount() + 1);
        documentRepository.save(doc);

        try {
            Resource resource = new UrlResource(filePath.toUri());
            String filename = filePath.getFileName().toString();
            String contentType = doc.getFileType();

            return new DownloadResponse(resource, filename, contentType);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + e.getMessage());
        }
    }

    @Transactional
    public void generateThumbnail(Document document) throws Exception {
        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        String filePath = uploadDir + document.getFileUrl();

        String fileName = document.getFileUrl();
        String extension = "";

        if (fileName.contains(".")) {
            extension = fileName.substring(fileName.lastIndexOf("."));
        }

        fileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".jpg";

        String thumbnailDir = System.getProperty("user.dir") + "/thumbnails/";
        String thumbnailPath = thumbnailDir + fileName;

        File thumbnailFolder = new File(thumbnailDir);
        if (!thumbnailFolder.exists()) {
            thumbnailFolder.mkdirs();
        }

        boolean generated = false;

        if (document.getFileType().contains("pdf")) {
            try (PDDocument pdf = PDDocument.load(new File(filePath))) {
                PDFRenderer renderer = new PDFRenderer(pdf);
                BufferedImage image = renderer.renderImage(0, 1.5f);
                ImageIO.write(image, "jpg", new File(thumbnailPath));
                generated = true;
            } catch (Exception e) {
                System.err.println("PDF thumbnail error: " + e.getMessage());
            }
        } else if (document.getFileType().contains("image")) {
            BufferedImage original = ImageIO.read(new File(filePath));
            if (original != null) {
                BufferedImage thumbnail = resizeImage(original, 200, 280);
                ImageIO.write(thumbnail, "jpg", new File(thumbnailPath));
                generated = true;
            }
        }

        if (!generated) {
            generated = createDefaultThumbnail(thumbnailPath, document.getFileType());
        }

        if (generated) {
            document.setThumbnailUrl("/thumbnails/" + fileName);
            documentRepository.save(document);
            System.out.println("Thumbnail created for: " + document.getTitle());
        }
    }

    private boolean createDefaultThumbnail(String thumbnailPath, String fileType) {
        try {
            int width = 200;
            int height = 280;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();

            g.setColor(new Color(240, 240, 240));
            g.fillRect(0, 0, width, height);

            g.setColor(new Color(100, 100, 100));
            g.setFont(new Font("Arial", Font.BOLD, 24));

            String text = "DOC";
            if (fileType.contains("pdf")) text = "PDF";
            else if (fileType.contains("word")) text = "DOC";
            else if (fileType.contains("excel")) text = "XLS";
            else text = "FILE";

            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();

            g.drawString(text, (width - textWidth) / 2, (height - textHeight) / 2 + textHeight/2);
            g.dispose();

            ImageIO.write(image, "jpg", new File(thumbnailPath));
            return true;
        } catch (Exception e) {
            System.err.println("Cannot create default thumbnail: " + e.getMessage());
            return false;
        }
    }

    private BufferedImage resizeImage(BufferedImage original, int targetWidth, int targetHeight) {
        BufferedImage resized = new BufferedImage(targetWidth, targetHeight, original.getType());
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, targetWidth, targetHeight, null);
        g.dispose();
        return resized;
    }

    private DocumentResponse mapToDocumentResponse(Document doc) {
        String webUrl = "/api/files/" + doc.getFileUrl();

        String thumbnailUrl = doc.getThumbnailUrl();

        if (thumbnailUrl == null || thumbnailUrl.isEmpty()) {
            // Thử tìm file thumbnail theo tên
            String thumbnailPath = "/thumbnails/" + doc.getFileUrl().replace(".pdf", ".jpg").replace(".docx", ".jpg");
            thumbnailUrl = thumbnailPath;
        }

        return new DocumentResponse(
                doc.getDocumentId(),
                doc.getTitle(),
                webUrl,
                thumbnailUrl,
                doc.getFileType(),
                doc.getFileSize(),
                doc.getCreatedAt(),
                doc.getUser().getSchool().getSchoolName()
        );
    }
}