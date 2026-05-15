package com.example.studyhub.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId")
    private Integer userId;

    @Column(name = "FullName", nullable = false)
    private String fullName;

    @Column(name = "Email", nullable = false, unique = true)
    private String email;

    @Column(name = "PasswordHash", nullable = false)
    private String passwordHash;

    @Column(name = "AvatarUrl")
    private String avatarUrl;

    @Column(name = "Bio")
    private String bio;

    @Column(name = "Role")
    private String role;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "SchoolId")
    private School school;

    public User(
            String fullName,
            String email,
            String passwordHash,
            String avatarUrl,
            String bio,
            String role,
            LocalDateTime createdAt,
            School school
    ) {
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.avatarUrl = avatarUrl;
        this.bio = bio;
        this.role = role;
        this.createdAt = createdAt;
        this.school = school;
    }
}