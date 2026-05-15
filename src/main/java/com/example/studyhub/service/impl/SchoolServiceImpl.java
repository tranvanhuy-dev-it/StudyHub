package com.example.studyhub.service.impl;

import com.example.studyhub.dto.request.AddSchoolRequest;
import com.example.studyhub.entities.School;
import com.example.studyhub.exception.AlreadyExistsException;
import com.example.studyhub.exception.NotFoundException;
import com.example.studyhub.repository.SchoolRepository;
import com.example.studyhub.service.SchoolService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SchoolServiceImpl implements SchoolService {
    private final SchoolRepository schoolRepository;

    public SchoolServiceImpl(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    @Override
    public List<School> getSchools() {
        return schoolRepository.findAll();
    }

    @Override
    public School getById(int id) {
        return schoolRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("School not found"));
    }

    @Override
    public void deleteById(int id) {
        if (!schoolRepository.existsById(id)) {
            throw new NotFoundException("School Not Found");
        }

        schoolRepository.deleteById(id);
    }

    @Override
    public School update(School school) {
        School existingSchool = schoolRepository.findById(school.getSchoolId())
                .orElseThrow(() -> new NotFoundException("School not found"));

        existingSchool.setSchoolName(school.getSchoolName());
        existingSchool.setAddress(school.getAddress());
        existingSchool.setWebsite(school.getWebsite());

        return schoolRepository.save(existingSchool);
    }

    @Override
    public School addSchool(AddSchoolRequest request) {
        if (schoolRepository.existsBySchoolName((request.getSchoolName()))) {
            throw new AlreadyExistsException("School is existing");
        }

        School school = new School();
        school.setSchoolName(request.getSchoolName());
        school.setWebsite(request.getWebsite());
        school.setAddress(request.getAddress());
        school.setCreatedAt(LocalDateTime.now());

        return schoolRepository.save(school);
    }
}
