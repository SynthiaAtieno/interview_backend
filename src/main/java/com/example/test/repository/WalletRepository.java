package com.example.test.repository;

import com.example.test.entities.Users;
import com.example.test.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface WalletRepository extends JpaRepository<Wallet,Long> {
    Optional<Wallet> findByUsers(Users users);
}


