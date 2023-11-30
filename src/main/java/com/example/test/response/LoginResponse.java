package com.example.test.response;

import com.example.test.entities.Users;
import com.example.test.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponse {
	private String token;
	private Role role;
	private Integer code;
	private String message;
	private Users user;
}
