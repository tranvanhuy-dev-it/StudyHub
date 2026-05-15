package com.example.studyhub.repository;

import com.example.studyhub.entities.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Integer> {
    boolean existsBySchoolName(String schoolName);
}
