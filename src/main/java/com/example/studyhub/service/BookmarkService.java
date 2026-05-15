package com.example.studyhub.service;

import com.example.studyhub.dto.request.AddBookmarkRequest;
import com.example.studyhub.dto.request.AddLikeRequest;
import com.example.studyhub.dto.response.BookmarkResponse;
import com.example.studyhub.entities.Bookmark;

import java.util.List;

public interface BookmarkService {
    BookmarkResponse add(AddBookmarkRequest request);
    List<BookmarkResponse> getAll();
    void delete(Integer id);
}