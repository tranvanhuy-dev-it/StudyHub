package com.example.studyhub.service;

import com.example.studyhub.dto.request.AddLikeRequest;
import com.example.studyhub.dto.response.LikeResponse;
import com.example.studyhub.entities.Like;

import java.util.List;

public interface LikeService {
    LikeResponse toggleLike(int userId, int documentId);
    boolean isLike(int userId, int documentId);
}