package com.example.studyhub.dto.request;

import com.example.studyhub.entities.School;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddSubjectRequest {

    private String subjectName;

    private String description;

    private int schoolId;
}
