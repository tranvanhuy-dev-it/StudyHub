package com.example.studyhub.service.impl;

import com.example.studyhub.dto.request.RegisterRequest;
import com.example.studyhub.entities.School;
import com.example.studyhub.exception.AlreadyExistsException;
import com.example.studyhub.exception.NotFoundException;
import com.example.studyhub.exception.WrongPasswordException;
import com.example.studyhub.dto.response.LoginResponse;
import com.example.studyhub.entities.User;
import com.example.studyhub.repository.SchoolRepository;
import com.example.studyhub.repository.UserRepository;
import com.example.studyhub.service.AuthService;
import com.example.studyhub.service.SchoolService;
import com.example.studyhub.utils.JWTService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final JWTService jwtService;

    public AuthServiceImpl(UserRepository userRepository, JWTService jwtService, SchoolRepository schoolRepository) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.schoolRepository = schoolRepository;
    }

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void register(RegisterRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("User is null");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("User is existed");
        }

        School school = schoolRepository.findById(request.getSchoolId())
                .orElseThrow(() -> new RuntimeException("School not found"));;
        String hashedPassword = passwordEncoder.encode(request.getPasswordHash());

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setCreatedAt(LocalDateTime.now());
        user.setPasswordHash(hashedPassword);
        user.setSchool(school);

        userRepository.save(user);
    }

    @Override
    public LoginResponse login(String email, String password) {
        if (email == null || password == null) {
            throw new IllegalArgumentException("Email or Password is null");
        }

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new NotFoundException("User not found");
        }

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new WrongPasswordException("Wrong password");
        }

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole()
        );

        return new LoginResponse(user.getUserId(), user.getEmail(), user.getFullName(), token);
    }
}
