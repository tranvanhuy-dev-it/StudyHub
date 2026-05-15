package com.example.studyhub.service.impl;

import com.example.studyhub.dto.request.AddSubjectRequest;
import com.example.studyhub.dto.response.SubjectResponse;
import com.example.studyhub.entities.School;
import com.example.studyhub.entities.Subject;
import com.example.studyhub.exception.AlreadyExistsException;
import com.example.studyhub.exception.NotFoundException;
import com.example.studyhub.repository.SchoolRepository;
import com.example.studyhub.repository.SubjectRepository;
import com.example.studyhub.service.SubjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;
    private final SchoolRepository schoolRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository, SchoolRepository schoolRepository) {
        this.subjectRepository = subjectRepository;
        this.schoolRepository = schoolRepository;
    }

    @Override
    public List<SubjectResponse> getSubjects(Integer schoolId) {
        List<Subject> subjects =  subjectRepository.findAll();

        return subjects.stream()
                .filter(subject -> schoolId == null ||
                        (subject.getSchool() != null &&
                                subject.getSchool().getSchoolId().equals(schoolId)))
                .map(this::mapToSubjectResponse)
                .toList();
    }

    @Override
    public SubjectResponse getById(int id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("School not found"));

        return mapToSubjectResponse(subject);
    }

    @Override
    public void deleteById(int id) {
        if (!subjectRepository.existsById(id)) {
            throw new NotFoundException("Subject Not Found");
        }

        subjectRepository.deleteById(id);
    }

    @Override
    public SubjectResponse update(Subject subject) {
        Subject existingSubject = subjectRepository.findById(subject.getSubjectId())
                .orElseThrow(() -> new NotFoundException("School not found"));

        existingSubject.setSubjectName(subject.getSubjectName());
        existingSubject.setDescription(subject.getDescription());
        existingSubject.setSchool(subject.getSchool());

        return mapToSubjectResponse(subjectRepository.save(existingSubject));
    }

    @Override
    public SubjectResponse addSubject(AddSubjectRequest request) {
        if (subjectRepository.existsBySubjectName((request.getSubjectName()))) {
            throw new AlreadyExistsException("School is existing");
        }

        School school = schoolRepository.findById(request.getSchoolId())
                .orElseThrow(() -> new RuntimeException("School not found"));

        Subject subject = new Subject();
        subject.setSubjectName(request.getSubjectName());
        subject.setDescription(request.getDescription());
        subject.setSchool(school);

        return mapToSubjectResponse(subjectRepository.save(subject));
    }

    private SubjectResponse mapToSubjectResponse(Subject subject) {
        return new SubjectResponse(
                subject.getSubjectId(),
                subject.getSubjectName(),
                subject.getDescription(),
                subject.getSchool().getSchoolId(),
                subject.getSchool().getSchoolName()
        );
    }
}
