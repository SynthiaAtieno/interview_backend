package com.example.test.contollers;

import com.example.test.dto.RegistrationDto;
import com.example.test.dto.TransactionDto;
import com.example.test.entities.Users;
import com.example.test.entities.Wallet;
import com.example.test.enums.Role;
import com.example.test.exception.BadRequestException;
import com.example.test.exception.NotFoundException;
import com.example.test.response.GenericResponse;
import com.example.test.response.RegisterResponse;
import com.example.test.response.WalletResponse;
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
    public WalletResponse deposit(@RequestBody TransactionDto transactionDto) {
        if (transactionDto != null) {
            Optional<Wallet> walletOptional = transactionService.findById(transactionDto.getUser());
            if (walletOptional.isPresent()) {
                Wallet wallet = walletOptional.get();
                var amount = (wallet.getBalance() != null ? wallet.getBalance() : 0) + transactionDto.getAmount();
                wallet.setBalance(amount);

                transactionService.save(wallet);

                return new WalletResponse(200, "Deposit Successful", amount);
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
                return new WalletResponse(200, "Deposit Successful", null);
            }
        }

        return new WalletResponse(400, "please fill all the details", null);
    }

    @PostMapping("withdraw")
    public WalletResponse withdraw(@RequestBody TransactionDto transactionDto) {
        if (transactionDto != null){
            Optional<Wallet> walletOptional = transactionService.findById(transactionDto.getUser());
            if (walletOptional.isPresent()) {
                Wallet wallet = walletOptional.get();
                if(wallet.getBalance() != null && wallet.getBalance() >= transactionDto.getAmount()){
                    var amount = wallet.getBalance() - transactionDto.getAmount();
                    wallet.setBalance(amount);

                    transactionService.save(wallet);

                    return new WalletResponse(200, "Withdraw Successful", amount);
                }else{
                    throw new BadRequestException("You don't have sufficient funds  to withdraw "+transactionDto.getAmount());
                }

            }
            throw new NotFoundException("User not found");
        }

        return new WalletResponse(400, "please fill all the details", null);
    }
}

