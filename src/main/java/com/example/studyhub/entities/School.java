package com.example.studyhub.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "schools")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SchoolId")
    private Integer schoolId;

    @Column(name = "SchoolName", nullable = false)
    private String schoolName;

    @Column(name = "Address")
    private String address;

    @Column(name = "Website")
    private String website;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;
}