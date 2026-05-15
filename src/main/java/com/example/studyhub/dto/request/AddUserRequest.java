package com.example.studyhub.dto.request;

import com.example.studyhub.entities.School;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddUserRequest {
    private String fullName;

    private String email;

    private String passwordHash;

    private String avatarUrl;

    private String bio;

    private String role;

    private int schoolId;
}
