package com.example.studyhub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponse {
    private Integer documentId;

    private String title;

    private String fileUrl;

    private String thumbnailUrl;

    private String fileType;

    private Long fileSize;

    private LocalDateTime createdAt;

    private String schoolName;
}
