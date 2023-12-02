package com.example.test.service.transactions;

import com.example.test.entities.Users;
import com.example.test.entities.Wallet;
import com.example.test.repository.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Transactional
@NoArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private WalletRepository walletRepository;

    @Override
    public Wallet save(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @Override
    public Optional<Wallet> findById(Long id) {
        return walletRepository.findById(id);
    }

    @Override
    public Optional<Wallet> findByUser(Users users) {
        return walletRepository.findByUsers(users);
    }

}
