package com.example.studyhub.controller;

import com.example.studyhub.dto.request.AddSubjectRequest;
import com.example.studyhub.dto.response.SubjectResponse;
import com.example.studyhub.entities.Subject;
import com.example.studyhub.service.SubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public ResponseEntity<List<SubjectResponse>> getAllSubjects(@RequestParam(required = false) Integer schoolId) {
        return ResponseEntity.ok(subjectService.getSubjects(schoolId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponse> getById(@PathVariable int id) {
        return ResponseEntity.ok(subjectService.getById(id));
    }

    @PostMapping
    public ResponseEntity<SubjectResponse> addSubject(@RequestBody AddSubjectRequest request) {
        return ResponseEntity.ok(subjectService.addSubject(request));
    }

    @PutMapping
    public ResponseEntity<SubjectResponse> updateSubject(@RequestBody Subject subject) {
        return ResponseEntity.ok(subjectService.update(subject));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubject(@PathVariable int id) {
        subjectService.deleteById(id);
        return ResponseEntity.ok("Delete subject successfully");
    }
}