package com.example.demo.dto.requestDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {
    private Long user_id;
    private Long course_id;
    private String payment_status;
    private String transaction_id;

}
