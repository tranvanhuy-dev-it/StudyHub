package com.example.studyhub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Integer commentId;

    private String content;

    private LocalDateTime createdAt;

    private int userId;

    private String userName;
}
