package com.example.studyhub.controller;

import com.example.studyhub.annotation.CurrentUserId;
import com.example.studyhub.dto.response.BookmarksResponse;
import com.example.studyhub.dto.response.PageResult;
import com.example.studyhub.service.BookmarkService;
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
}