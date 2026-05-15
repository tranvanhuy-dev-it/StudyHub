package com.example.studyhub.controller;

import com.example.studyhub.dto.request.AddSchoolRequest;
import com.example.studyhub.entities.School;
import com.example.studyhub.service.SchoolService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schools")
public class SchoolController {
    private final SchoolService schoolService;

    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<School> getSchool(@PathVariable Integer id) {
        return ResponseEntity.ok(schoolService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<School>> getSchools() {
        return ResponseEntity.ok(schoolService.getSchools());
    }

//    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSchool(@PathVariable Integer id) {
        schoolService.deleteById(id);
        return ResponseEntity.ok("Done");
    }

//    @PreAuthorize("hasRole('Admin')")
    @PostMapping
    public ResponseEntity<School> addSchool(@RequestBody AddSchoolRequest request) {
        return ResponseEntity.ok(schoolService.addSchool(request));
    }

    @PutMapping
    public ResponseEntity<School> updateSchool(@RequestBody School request) {
        return ResponseEntity.ok(schoolService.update(request));
    }
}
