package com.example.studyhub.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TagId")
    private Integer tagId;

    @Column(name = "TagName", nullable = false, unique = true)
    private String tagName;
}