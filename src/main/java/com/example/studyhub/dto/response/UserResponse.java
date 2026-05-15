package com.example.studyhub.dto.response;

import com.example.studyhub.entities.School;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private int userId;

    private String fullName;

    private String email;

    private String avatarUrl;

    private String bio;

    private String role;

    private LocalDateTime createdAt;

    private int schoolId;

    private String schoolName;
}
