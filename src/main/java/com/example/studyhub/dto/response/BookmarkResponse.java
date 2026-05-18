package com.example.studyhub.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkResponse {
    @JsonProperty("bookmarked")
    private boolean isBoookmark;

    @JsonProperty("count")
    private int bookmarkCount;
}