package com.example.test.service.user;

import com.example.test.entities.Users;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {

    Users save(Users users);
    Optional<Users> findById(Long id);

    Optional<Users> findByEmail(String email);
}
