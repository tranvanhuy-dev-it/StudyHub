package com.example.studyhub.service;

import com.example.studyhub.dto.request.AddTagRequest;
import com.example.studyhub.entities.Tag;

import java.util.List;

public interface TagService {
    Tag create(AddTagRequest tag);
    List<Tag> getAll();
    Tag getById(Integer id);
    void delete(Integer id);
}
