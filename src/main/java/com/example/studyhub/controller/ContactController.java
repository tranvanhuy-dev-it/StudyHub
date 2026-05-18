package com.example.studyhub.controller;

import com.example.studyhub.dto.request.ContactRequest;
import com.example.studyhub.dto.response.ContactResponse;
import com.example.studyhub.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    // ========== CÔNG KHAI ==========
    @PostMapping
    public ResponseEntity<Map<String, Object>> submitContact(@Valid @RequestBody ContactRequest request) {
        ContactResponse response = contactService.createContact(request);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Tin nhắn đã được gửi thành công!");
        result.put("data", response);

        return ResponseEntity.ok(result);
    }

    // ========== ADMIN ==========
    @GetMapping("/admin/all")
    public ResponseEntity<Page<ContactResponse>> getAllContacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        return ResponseEntity.ok(contactService.getAllContacts(pageable));
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<ContactResponse> getContactById(@PathVariable Integer id) {
        return ResponseEntity.ok(contactService.getContactById(id));
    }

    @GetMapping("/admin/email/{email}")
    public ResponseEntity<List<ContactResponse>> getContactsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(contactService.getContactsByEmail(email));
    }

    @GetMapping("/admin/status/{status}")
    public ResponseEntity<List<ContactResponse>> getContactsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(contactService.getContactsByStatus(status));
    }

    @GetMapping("/admin/pending-count")
    public ResponseEntity<Map<String, Long>> getPendingCount() {
        Map<String, Long> response = new HashMap<>();
        response.put("count", contactService.countPendingContacts());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/admin/{id}/status")
    public ResponseEntity<ContactResponse> updateStatus(
            @PathVariable Integer id,
            @RequestBody Map<String, String> payload) {
        return ResponseEntity.ok(contactService.updateStatus(id, payload.get("status")));
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Map<String, String>> deleteContact(@PathVariable Integer id) {
        contactService.deleteContact(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Xóa tin nhắn thành công!");
        return ResponseEntity.ok(response);
    }
}