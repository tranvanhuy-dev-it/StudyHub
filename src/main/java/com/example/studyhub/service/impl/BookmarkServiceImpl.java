package com.example.studyhub.service.impl;

import com.example.studyhub.dto.request.AddBookmarkRequest;
import com.example.studyhub.dto.response.BookmarkResponse;
import com.example.studyhub.entities.Bookmark;
import com.example.studyhub.entities.Document;
import com.example.studyhub.entities.User;
import com.example.studyhub.repository.BookmarkRepository;
import com.example.studyhub.repository.DocumentRepository;
import com.example.studyhub.repository.UserRepository;
import com.example.studyhub.service.BookmarkService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    public BookmarkServiceImpl(BookmarkRepository bookmarkRepository,
                               DocumentRepository documentRepository, UserRepository userRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public BookmarkResponse add(AddBookmarkRequest request) {
        Document doc = documentRepository.findById(request.getDocumentId())
                .orElseThrow(() -> new RuntimeException("Document not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Bookmark bm = new Bookmark();
        bm.setCreatedAt(LocalDateTime.now());
        bm.setDocument(doc);
        bm.setUser(user);

        Bookmark saved = bookmarkRepository.save(bm);

        return new BookmarkResponse(
                saved.getId(),
                saved.getCreatedAt(),
                saved.getUser().getUserId(),
                saved.getUser().getFullName(),
                saved.getDocument().getDocumentId(),
                saved.getDocument().getTitle()
        );
    }

    @Override
    public List<BookmarkResponse> getAll() {
        List<Bookmark> bms = bookmarkRepository.findAll();

        return bms.stream().map(bm -> new BookmarkResponse(
                bm.getId(),
                bm.getCreatedAt(),
                bm.getUser().getUserId(),
                bm.getUser().getFullName(),
                bm.getDocument().getDocumentId(),
                bm.getDocument().getTitle()
        )).toList();
    }

    @Override
    public void delete(Integer id) {
        bookmarkRepository.deleteById(id);
    }
}