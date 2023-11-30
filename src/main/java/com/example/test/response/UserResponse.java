package com.example.test.response;

import com.example.test.entities.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponse {
	private int code;
	private String message;
	private Users user;
}
