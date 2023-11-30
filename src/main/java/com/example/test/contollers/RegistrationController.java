package com.example.test.contollers;

import com.example.test.dto.RegistrationDto;
import com.example.test.entities.Users;
import com.example.test.enums.Role;
import com.example.test.response.RegisterResponse;
import com.example.test.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping
    public RegisterResponse register(@RequestBody RegistrationDto registrationDto) {
        if (registrationDto.getEmail() != null && registrationDto.getName() != null && registrationDto.getPassword() != null){
            Optional<Users> userOp = userService.findByEmail(registrationDto.getEmail());
            if (userOp.isEmpty()) {
                Users user = new Users();
                user.setName(registrationDto.getName());
                user.setEmail(registrationDto.getEmail());
                user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
                user.setRole(Role.ROLE_USER);
                user.setStatus("PENDING");
                Users response = userService.save(user);

                return new RegisterResponse(200, "Registration Successful", response);
            } else return new RegisterResponse(432, "User with email already exist", userOp.get());

        }
        return new RegisterResponse(432, "Please fill all the fields", null);
    }
}

