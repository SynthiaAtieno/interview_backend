package com.example.test.repository;

import com.example.test.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByEmail(String email);
}


