package com.example.studyhub.service;

import com.example.studyhub.dto.response.BookmarkResponse;
import com.example.studyhub.dto.response.BookmarksResponse;
import com.example.studyhub.dto.response.PageResult;

public interface BookmarkService {

    BookmarkResponse toggleBookmark(int userId, int documentId);

    PageResult<BookmarksResponse> getByUserId(int userId, int page, int pageSize);

    int getBookmarkCount(int documentId);

    boolean isBookmarked(int userId, int documentId);

    void delete(Integer id);

    boolean isBookmark(int userId, int documentId);
}