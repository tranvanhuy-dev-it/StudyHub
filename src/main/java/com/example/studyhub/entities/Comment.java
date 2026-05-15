package com.example.studyhub.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CommentId")
    private Integer commentId;

    @Column(name = "Content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "DocumentId", nullable = false)
    private Document document;

    @ManyToOne
    @JoinColumn(name = "UserId", nullable = false)
    private User user;
}