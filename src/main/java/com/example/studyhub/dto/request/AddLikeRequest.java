package com.example.studyhub.dto.request;

import com.example.studyhub.entities.Document;
import com.example.studyhub.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddLikeRequest {
    private int userId;

    private int documentId;
}
