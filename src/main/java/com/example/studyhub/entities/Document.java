package com.example.studyhub.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DocumentId")
    private Integer documentId;

    @Column(name = "Title", nullable = false)
    private String title;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "FileUrl", nullable = false)
    private String fileUrl;

    @Column(name = "FileType")
    private String fileType;

    @Column(name = "FileSize")
    private Long fileSize;

    @Column(name = "ThumbnailUrl")
    private String thumbnailUrl;

    @Column(name = "DownloadCount")
    private Integer downloadCount = 0;

    @Column(name = "ViewCount")
    private Integer viewCount = 0;

    @Column(name = "Status")
    private String status = "PENDING";

    @Column(name = "Visibility")
    private String visibility = "PUBLIC";

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "UserId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "SubjectId", nullable = false)
    private Subject subject;

    @ManyToMany
    @JoinTable(
            name = "document_tags",
            joinColumns = @JoinColumn(name = "DocumentId"),
            inverseJoinColumns = @JoinColumn(name = "TagId")
    )
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "document")
    private Set<Like> likes = new HashSet<>();

    @OneToMany(mappedBy = "document")
    private Set<Comment> comments = new HashSet<>();
}