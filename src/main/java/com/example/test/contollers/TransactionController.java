package com.example.test.contollers;

import com.example.test.dto.RegistrationDto;
import com.example.test.dto.TransactionDto;
import com.example.test.entities.Users;
import com.example.test.entities.Wallet;
import com.example.test.enums.Role;
import com.example.test.exception.NotFoundException;
import com.example.test.response.GenericResponse;
import com.example.test.response.RegisterResponse;
import com.example.test.service.transactions.TransactionService;
import com.example.test.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    UserService userService;


    @PostMapping("deposit")
    public GenericResponse deposit(@RequestBody TransactionDto transactionDto) {
        Optional<Wallet> walletOptional = transactionService.findById(transactionDto.getUser());
        if (walletOptional.isPresent()) {
            Wallet wallet = walletOptional.get();
            var amount = (wallet.getBalance() != null ? wallet.getBalance() : 0) + transactionDto.getAmount();
            wallet.setBalance(amount);

            transactionService.save(wallet);

            return new GenericResponse(200, "Deposit Successful");
        }else{
            Optional<Users> usersOptional = userService.findById(transactionDto.getUser());
            if(usersOptional.isPresent()){
                Wallet wallet = new Wallet();
                wallet.setBalance(transactionDto.getAmount());
                wallet.setUsers(usersOptional.get());
                transactionService.save(wallet);
            }else{
                throw new NotFoundException("User not found");
            }
            return new GenericResponse(200, "Deposit Successful");
        }

    }

    @PostMapping("withdraw")
    public GenericResponse withdraw(@RequestBody TransactionDto transactionDto) {
        Optional<Wallet> walletOptional = transactionService.findById(transactionDto.getUser());
        if (walletOptional.isPresent()) {
            Wallet wallet = walletOptional.get();
            var amount = (wallet.getBalance() != null ? wallet.getBalance() : 0) - transactionDto.getAmount();
            wallet.setBalance(amount);

            transactionService.save(wallet);

            return new GenericResponse(200, "Withdraw Successful");
        }
        throw new NotFoundException("User not found");
    }
}

