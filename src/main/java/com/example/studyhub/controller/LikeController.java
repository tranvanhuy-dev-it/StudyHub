package com.example.studyhub.controller;

import com.example.studyhub.dto.request.AddLikeRequest;
import com.example.studyhub.entities.Like;
import com.example.studyhub.service.LikeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    // Thêm like
    @PostMapping
    public Like create(@RequestBody AddLikeRequest likerequest) {
        return likeService.create(likerequest);
    }

    // Lấy tất cả like
    @GetMapping
    public List<Like> getAll() {
        return likeService.getAll();
    }

    // Xoá like
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        likeService.delete(id);
    }
}