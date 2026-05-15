package com.example.studyhub.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Subjects")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SubjectId")
    private Integer subjectId;

    @Column(name = "SubjectName", nullable = false)
    private String subjectName;

    @Column(name = "Description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "SchoolId", nullable = false)
    private School school;
}