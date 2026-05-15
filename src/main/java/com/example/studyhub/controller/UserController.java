package com.example.studyhub.controller;

import com.example.studyhub.dto.request.AddUserRequest;
import com.example.studyhub.dto.response.UserDetailResponse;
import com.example.studyhub.dto.response.UserResponse;
import com.example.studyhub.entities.User;
import com.example.studyhub.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailResponse> getUser(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers(
            @RequestParam(required = false) int schoolId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ResponseEntity.ok(userService.getUsers(schoolId, page, pageSize));
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        userService.deleteById(id);
        return ResponseEntity.ok("Done");
    }

    @PreAuthorize("hasRole('Admin')")
    @PostMapping
    public ResponseEntity<UserResponse> addUser(@RequestBody AddUserRequest request) {
        return ResponseEntity.ok(userService.addUser(request));
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@RequestBody User request) {
        return ResponseEntity.ok(userService.update(request));
    }
}
