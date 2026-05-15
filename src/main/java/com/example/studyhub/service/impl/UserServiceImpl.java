package com.example.studyhub.service.impl;

import com.example.studyhub.dto.request.AddUserRequest;
import com.example.studyhub.dto.response.UserDetailResponse;
import com.example.studyhub.dto.response.UserResponse;
import com.example.studyhub.entities.School;
import com.example.studyhub.entities.User;
import com.example.studyhub.exception.AlreadyExistsException;
import com.example.studyhub.exception.NotFoundException;
import com.example.studyhub.repository.SchoolRepository;
import com.example.studyhub.repository.UserRepository;
import com.example.studyhub.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserRepository userRepository, SchoolRepository schoolRepository) {
        this.userRepository = userRepository;
        this.schoolRepository = schoolRepository;
    }

    @Override
    public List<UserResponse> getUsers(int schoolId, int page, int pageSize) {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    @Override
    public UserDetailResponse getById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return new UserDetailResponse(
                user.getUserId(),
                user.getFullName(),
                user.getEmail(),
                user.getAvatarUrl(),
                user.getBio(),
                user.getRole(),
                user.getCreatedAt(),
                user.getSchool()
        );
    }

    @Override
    public void deleteById(int id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found");
        }

        userRepository.deleteById(id);
    }

    @Override
    public UserResponse update(User user) {
        User existingUser = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setAvatarUrl(user.getAvatarUrl());
        existingUser.setBio(user.getBio());
        existingUser.setRole(user.getRole());

        User saved = userRepository.save(existingUser);

        return mapToUserResponse(saved);
    }

    @Override
    public UserResponse addUser(AddUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("User already exists");
        }

        String passwordHash = passwordEncoder.encode(request.getPasswordHash());

        School school = schoolRepository.getReferenceById(request.getSchoolId());

        User user = new User(
                request.getFullName(),
                request.getEmail(),
                passwordHash,
                request.getAvatarUrl(),
                request.getBio(),
                request.getRole(),
                LocalDateTime.now(),
                school
        );

        User saved = userRepository.save(user);

        return mapToUserResponse(saved);
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getFullName(),
                user.getEmail(),
                user.getAvatarUrl(),
                user.getBio(),
                user.getRole(),
                user.getCreatedAt(),
                user.getSchool().getSchoolId(),
                user.getSchool().getSchoolName());
    }
}
