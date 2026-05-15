package com.example.studyhub.service;

import com.example.studyhub.dto.request.AddUserRequest;
import com.example.studyhub.dto.response.UserDetailResponse;
import com.example.studyhub.dto.response.UserResponse;
import com.example.studyhub.entities.User;

import java.util.List;

public interface UserService {
    List<UserResponse> getUsers(int schoolId, int page, int pageSize);
    UserDetailResponse getById(int id);
    void deleteById(int id);
    UserResponse update(User user);
    UserResponse addUser(AddUserRequest request);
}
