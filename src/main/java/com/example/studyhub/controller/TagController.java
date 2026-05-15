package com.example.studyhub.controller;

import com.example.studyhub.dto.request.AddTagRequest;
import com.example.studyhub.entities.Tag;
import com.example.studyhub.service.TagService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public Tag create(@RequestBody AddTagRequest tag) {
        return tagService.create(tag);
    }

    @GetMapping
    public List<Tag> getAll() {
        return tagService.getAll();
    }

    @GetMapping("/{id}")
    public Tag getById(@PathVariable Integer id) {
        return tagService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        tagService.delete(id);
    }
}