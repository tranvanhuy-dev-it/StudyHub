package com.example.studyhub.service;

import com.example.studyhub.dto.request.ContactRequest;
import com.example.studyhub.dto.response.ContactResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContactService {

    ContactResponse createContact(ContactRequest request);

    List<ContactResponse> getAllContacts();

    Page<ContactResponse> getAllContacts(Pageable pageable);

    ContactResponse getContactById(Integer id);

    List<ContactResponse> getContactsByEmail(String email);

    List<ContactResponse> getContactsByStatus(String status);

    ContactResponse updateStatus(Integer id, String status);

    void deleteContact(Integer id);

    long countPendingContacts();
}