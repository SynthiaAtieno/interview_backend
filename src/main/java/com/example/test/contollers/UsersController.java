package com.example.test.contollers;

import com.example.test.dto.RegistrationDto;
import com.example.test.entities.Users;
import com.example.test.enums.Role;
import com.example.test.exception.NotFoundException;
import com.example.test.response.GenericResponse;
import com.example.test.response.RegisterResponse;
import com.example.test.response.UserResponse;
import com.example.test.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("update")
    public UserResponse update(@RequestBody RegistrationDto registrationDto) {
        Optional<Users> userOp = userService.findByEmail(registrationDto.getEmail());
        if (userOp.isPresent()) {
            Users user = userOp.get();
            if(registrationDto.getName() != null)user.setName(registrationDto.getName());
            if(registrationDto.getEmail() != null)user.setEmail(registrationDto.getEmail());
            if(registrationDto.getPassword() != null)user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
            var response = userService.save(user);

            return new UserResponse(200, "Successful", response);
        }
        throw new NotFoundException("User not found");

    }

}
