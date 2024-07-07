package com.example.demo.controller;


import com.example.demo.dto.requestDTO.UserRequest;
import com.example.demo.dto.responsDTO.UserSignUpResponse;
import com.example.demo.entity.UserEntity;
import com.example.demo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private IUserService userService;

    @PostMapping
    public UserSignUpResponse createUser(@RequestBody UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @GetMapping("/{id}")
    public UserEntity getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

}
