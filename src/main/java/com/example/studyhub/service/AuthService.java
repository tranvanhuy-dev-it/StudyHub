package com.example.studyhub.service;

import com.example.studyhub.dto.request.RegisterRequest;
import com.example.studyhub.dto.response.LoginResponse;
import com.example.studyhub.entities.User;

public interface AuthService {
    void register(RegisterRequest request);
    LoginResponse login(String email, String password);
}
