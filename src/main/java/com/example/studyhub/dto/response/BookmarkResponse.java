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
public class BookmarkResponse {
    private Integer id;

    private LocalDateTime createdAt;

    private int UserId;

    private String userName;

    private int DocumentId;

    private String Title;
}
