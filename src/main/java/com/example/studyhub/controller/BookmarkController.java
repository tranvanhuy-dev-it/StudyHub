package com.example.studyhub.controller;

import com.example.studyhub.dto.request.AddBookmarkRequest;
import com.example.studyhub.dto.response.BookmarkResponse;
import com.example.studyhub.entities.Bookmark;
import com.example.studyhub.service.BookmarkService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping
    public BookmarkResponse add(@RequestBody AddBookmarkRequest request) {
        return bookmarkService.add(request);
    }

    @GetMapping
    public List<BookmarkResponse> getAll() {
        return bookmarkService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        bookmarkService.delete(id);
    }
}