package com.example.test.response;

import com.example.test.entities.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsersResponse {
	private int code;
	private String message;
	private List<Users> users;
}
