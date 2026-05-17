package com.example.studyhub.repository;

import com.example.studyhub.entities.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    boolean existsBySubjectName(String subjectName);
    List<Subject> findBySchool_SchoolId(Integer schoolId);
    Page<Subject> findBySchool_SchoolId(Integer schoolId, Pageable pageable);
    Page<Subject> findBySchool_SchoolIdAndSubjectNameContainingIgnoreCase(Integer schoolId, String search, Pageable pageable);
    Page<Subject> findBySubjectNameContainingIgnoreCase(String search, Pageable pageable);
}
