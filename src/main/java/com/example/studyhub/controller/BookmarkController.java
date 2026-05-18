package com.example.studyhub.controller;

import com.example.studyhub.annotation.CurrentUserId;
import com.example.studyhub.dto.response.BookmarkResponse;
import com.example.studyhub.dto.response.BookmarksResponse;
import com.example.studyhub.dto.response.PageResult;
import com.example.studyhub.service.BookmarkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @GetMapping("/me")
    public PageResult<BookmarksResponse> getAll(
            @CurrentUserId Integer userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return bookmarkService.getByUserId(userId, page, pageSize);
    }

    @PostMapping("/{documentId}")
    public ResponseEntity<?> toggleBookmark(
            @CurrentUserId Integer userId,
            @PathVariable int documentId
    ) {
        BookmarkResponse bookmark = bookmarkService.toggleBookmark(userId, documentId);
        return ResponseEntity.ok(bookmark);
    }

    @GetMapping("status/{documentId}")
    public ResponseEntity<Boolean> isBookmarked(
            @PathVariable int documentId,
            @CurrentUserId Integer userId) {

        if (userId == null) {
            return ResponseEntity.ok(false);
        }

        boolean isBookmarked = bookmarkService.isBookmark(userId, documentId);
        return ResponseEntity.ok(isBookmarked);
    }
}