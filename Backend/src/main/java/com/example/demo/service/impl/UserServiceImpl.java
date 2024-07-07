package com.example.demo.service.impl;

import com.example.demo.converter.UserConverter;
import com.example.demo.dto.requestDTO.UserRequest;
import com.example.demo.dto.responsDTO.UserSignUpResponse;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter userConverter;

    @Override
    public UserSignUpResponse createUser(UserRequest user) {
        UserEntity userEntity = userConverter.convertToEntity(user);
        userRepository.save(userEntity);
        UserSignUpResponse userSignUpResponse = new UserSignUpResponse();
        UserSignUpResponse.Meta meta = new UserSignUpResponse.Meta();
        meta.setCode("200001");
        meta.setMessage("User created successfully");
        UserSignUpResponse.Data data = userConverter.convertToResponse(userEntity);
        userSignUpResponse.setMeta(meta);
        userSignUpResponse.setData(data);
        return userSignUpResponse;
    }

    @Override
    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

}
