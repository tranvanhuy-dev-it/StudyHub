package com.example.studyhub.repository;

import com.example.studyhub.entities.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

    List<Contact> findByEmail(String email);

    Page<Contact> findByEmail(String email, Pageable pageable);

    List<Contact> findByStatus(Contact.Status status);

    Page<Contact> findByStatus(Contact.Status status, Pageable pageable);

    long countByStatus(Contact.Status status);

    @Query("SELECT c FROM Contact c WHERE c.status = :status ORDER BY c.createdAt DESC")
    List<Contact> findPendingContacts(@Param("status") Contact.Status status);

    @Query("SELECT c FROM Contact c WHERE c.name LIKE %:keyword% OR c.email LIKE %:keyword%")
    Page<Contact> search(@Param("keyword") String keyword, Pageable pageable);
}