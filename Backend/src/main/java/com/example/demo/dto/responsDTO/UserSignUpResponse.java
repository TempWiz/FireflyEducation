package com.example.demo.dto.responsDTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserSignUpResponse {
    private Meta meta;
    private Data data;
    @Getter
    @Setter
    public static class Meta {
        private String code;
        private String message;
    }
    @Getter
    @Setter
    public static class Data {
        private Long id;
        private String name;
        private Date dob;
        private String username;
    }
}
