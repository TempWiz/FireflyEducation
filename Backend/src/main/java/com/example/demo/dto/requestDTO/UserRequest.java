package com.example.demo.dto.requestDTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter

public class UserRequest {
    private String name;
    private Date dob;
    private String username;
    private String password;
}
