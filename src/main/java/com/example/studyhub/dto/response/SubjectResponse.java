package com.example.studyhub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectResponse {
    private Integer subjectId;

    private String subjectName;

    private String description;

    private int schoolId;

    private String SchoolName;
}
