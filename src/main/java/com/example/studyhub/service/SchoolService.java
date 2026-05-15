package com.example.studyhub.service;

import com.example.studyhub.dto.request.AddSchoolRequest;
import com.example.studyhub.entities.School;

import java.util.List;

public interface SchoolService {
    List<School> getSchools();
    School getById(int id);
    void deleteById(int id);
    School update(School user);
    School addSchool(AddSchoolRequest request);
}
