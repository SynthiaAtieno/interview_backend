package com.example.test.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletResponse {

    private int code;
    private String message;
    Double balance;
}
