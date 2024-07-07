package com.example.demo.service;

import com.example.demo.dto.requestDTO.UserRequest;
import com.example.demo.dto.responsDTO.UserSignUpResponse;
import com.example.demo.entity.UserEntity;

public interface IUserService {
    UserSignUpResponse createUser(UserRequest user);
    UserEntity getUserById(Long id);
}
