package com.example.studyhub.service;

import com.example.studyhub.dto.request.AddLikeRequest;
import com.example.studyhub.entities.Like;

import java.util.List;

public interface LikeService {
    Like create(AddLikeRequest request);
    List<Like> getAll();
    void delete(Integer id);
}