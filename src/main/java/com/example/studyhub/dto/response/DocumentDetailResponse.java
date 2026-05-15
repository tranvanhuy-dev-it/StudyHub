package com.example.studyhub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDetailResponse {
    private Integer documentId;

    private String title;

    private String description;

    private String fileUrl;

    private String fileType;

    private Long fileSize;

    private String thumbnailUrl;

    private Integer downloadCount = 0;

    private Integer viewCount = 0;

    private String status = "PENDING";

    private String visibility = "PUBLIC";

    private LocalDateTime createdAt;

    private Integer likeCount;

    private int userId;

    private String userName;

    private int SubjectId;

    private String SubjectName;

    private int schoolId;

    private String schoolName;
}
