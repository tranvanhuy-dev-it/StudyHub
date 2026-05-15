package com.example.studyhub.service.impl;

import com.example.studyhub.dto.request.AddTagRequest;
import com.example.studyhub.entities.Tag;
import com.example.studyhub.repository.TagRepository;
import com.example.studyhub.service.TagService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag create(AddTagRequest request) {
        Tag tag = new Tag();
        tag.setTagName(request.getTagName());
        return tagRepository.save(tag);
    }

    @Override
    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    @Override
    public Tag getById(Integer id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
    }

    @Override
    public void delete(Integer id) {
        tagRepository.deleteById(id);
    }
}