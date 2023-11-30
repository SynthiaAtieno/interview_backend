package com.example.test.service.transactions;

import com.example.test.entities.Users;
import com.example.test.entities.Wallet;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface TransactionService {

    Wallet save(Wallet wallet);

    Optional<Wallet> findById(Long id);

    Optional<Wallet> findByUser(Users users);
}
