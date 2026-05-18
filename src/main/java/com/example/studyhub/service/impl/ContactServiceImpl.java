package com.example.studyhub.service.impl;

import com.example.studyhub.dto.request.ContactRequest;
import com.example.studyhub.dto.response.ContactResponse;
import com.example.studyhub.entities.Contact;
import com.example.studyhub.repository.ContactRepository;
import com.example.studyhub.service.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    @Override
    public ContactResponse createContact(ContactRequest request) {
        Contact contact = Contact.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .subject(request.getSubject())
                .message(request.getMessage())
                .status(Contact.Status.PENDING)
                .build();

        contact.setCreatedAt(LocalDateTime.now());
        contact.setUpdatedAt(LocalDateTime.now());

        Contact saved = contactRepository.save(contact);
        log.info("Created new contact from: {}", request.getEmail());

        return convertToResponse(saved);
    }

    @Override
    public List<ContactResponse> getAllContacts() {
        return contactRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ContactResponse> getAllContacts(Pageable pageable) {
        return contactRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    @Override
    public ContactResponse getContactById(Integer id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tin nhắn với id: " + id));
        return convertToResponse(contact);
    }

    @Override
    public List<ContactResponse> getContactsByEmail(String email) {
        return contactRepository.findByEmail(email)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContactResponse> getContactsByStatus(String status) {
        Contact.Status contactStatus = Contact.Status.valueOf(status.toUpperCase());
        return contactRepository.findByStatus(contactStatus)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ContactResponse updateStatus(Integer id, String status) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tin nhắn với id: " + id));

        contact.setStatus(Contact.Status.valueOf(status.toUpperCase()));
        Contact updated = contactRepository.save(contact);

        log.info("Updated contact {} status to {}", id, status);
        return convertToResponse(updated);
    }

    @Override
    public void deleteContact(Integer id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tin nhắn với id: " + id));
        contactRepository.delete(contact);
        log.info("Deleted contact with id: {}", id);
    }

    @Override
    public long countPendingContacts() {
        return contactRepository.countByStatus(Contact.Status.PENDING);
    }

    private ContactResponse convertToResponse(Contact contact) {
        return ContactResponse.builder()
                .id(contact.getId())
                .name(contact.getName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .subject(contact.getSubject())
                .message(contact.getMessage())
                .status(contact.getStatus().name())
                .createdAt(contact.getCreatedAt())
                .updatedAt(contact.getUpdatedAt())
                .build();
    }
}