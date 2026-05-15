package com.example.studyhub.repository;

import com.example.studyhub.entities.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {

}
