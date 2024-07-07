package com.example.demo.dto.responsDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenBalanceResponse {
    private boolean success;
    private String message;
    private Result result;

    @Getter
    @Setter
    public static class Result {
        private double balance;
    }
}
