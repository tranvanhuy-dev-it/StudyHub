package com.example.studyhub.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddDocumentRequest {

    private String title;
    private String description;
    private String thumbnailUrl;

    private MultipartFile file;

    private String visibility;

    private Integer userId;
    private Integer subjectId;

    private Set<Integer> tagIds;
}