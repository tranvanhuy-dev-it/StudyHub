package com.example.studyhub.service;

import com.example.studyhub.dto.request.AddSubjectRequest;
import com.example.studyhub.dto.response.SubjectResponse;
import com.example.studyhub.entities.Subject;

import java.util.List;

public interface SubjectService {
    List<SubjectResponse> getSubjects(Integer schoolId);
    SubjectResponse getById(int id);
    void deleteById(int id);
    SubjectResponse update(Subject sub);
    SubjectResponse addSubject(AddSubjectRequest request);
}
