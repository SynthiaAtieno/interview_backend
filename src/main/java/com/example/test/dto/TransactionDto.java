package com.example.test.dto;

import lombok.Data;

@Data
public class TransactionDto {
    private Double amount;
    private String type;
    private Long user;
}
