package com.example.studyhub.service.impl;

import com.example.studyhub.dto.response.BookmarkResponse;
import com.example.studyhub.dto.response.BookmarksResponse;
import com.example.studyhub.dto.response.LikeResponse;
import com.example.studyhub.dto.response.PageResult;
import com.example.studyhub.entities.Bookmark;
import com.example.studyhub.entities.Document;
import com.example.studyhub.entities.Like;
import com.example.studyhub.entities.User;
import com.example.studyhub.repository.BookmarkRepository;
import com.example.studyhub.repository.DocumentRepository;
import com.example.studyhub.repository.UserRepository;
import com.example.studyhub.service.BookmarkService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    public BookmarkServiceImpl(BookmarkRepository bookmarkRepository,
                               DocumentRepository documentRepository,
                               UserRepository userRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public BookmarkResponse toggleBookmark(int userId, int documentId) {
        boolean isBookmark = bookmarkRepository.existsByUser_UserIdAndDocument_DocumentId(userId, documentId);

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        if (isBookmark) {
            bookmarkRepository.deleteByUser_UserIdAndDocument_DocumentId(userId, documentId);
            document.setBookmarkCount(document.getBookmarkCount() - 1);
            documentRepository.save(document);

            return new BookmarkResponse(false, document.getBookmarkCount());
        } else {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Bookmark bookmark = new Bookmark();
            bookmark.setCreatedAt(LocalDateTime.now());
            bookmark.setDocument(document);
            bookmark.setUser(user);
            bookmarkRepository.save(bookmark);

            document.setBookmarkCount(document.getBookmarkCount() + 1);
            documentRepository.save(document);

            return new BookmarkResponse(true, document.getBookmarkCount());
        }
    }

    @Override
    public PageResult<BookmarksResponse> getByUserId(int userId, int page, int pageSize) {

        Pageable pageable = PageRequest.of(page - 1, pageSize);

        Page<Bookmark> bookmarkPage =
                bookmarkRepository.findByUser_UserId(userId, pageable);

        List<BookmarksResponse> bookmarkResponses = bookmarkPage.getContent()
                .stream()
                .map(this::mapToBookmarkResponse)
                .toList();

        long total = bookmarkPage.getTotalElements();
        int totalPages = bookmarkPage.getTotalPages();

        return new PageResult<>(
                bookmarkResponses,
                total,
                totalPages,
                page,
                pageSize
        );
    }

    @Override
    public long getBookmarkCount(int documentId) {
        return bookmarkRepository.countByDocument_DocumentId(documentId);
    }

    @Override
    public boolean isBookmarked(int userId, int documentId) {
        return bookmarkRepository.existsByUser_UserIdAndDocument_DocumentId(userId, documentId);
    }

    @Override
    public void delete(Integer id) {
        bookmarkRepository.deleteById(id);
    }

    private BookmarksResponse mapToBookmarkResponse(Bookmark bm) {
        return new BookmarksResponse(
                bm.getId(),
                bm.getCreatedAt(),
                bm.getUser().getUserId(),
                bm.getUser().getFullName(),
                bm.getDocument().getDocumentId(),
                bm.getDocument().getTitle()
        );
    }

    @Override
    public boolean isBookmark(int userId, int documentId) {
        return bookmarkRepository.existsByUser_UserIdAndDocument_DocumentId(userId, documentId);
    }
}