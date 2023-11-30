package com.example.test;

import com.example.test.entities.Users;
import com.example.test.enums.Role;
import com.example.test.repository.UserRepository;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;

@SpringBootApplication
public class TestApplication implements ApplicationRunner, CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        TimeZone.setDefault(TimeZone.getTimeZone("Africa/Nairobi"));

        SpringApplication.run(TestApplication.class, args);
    }

    @Bean
    public OkHttpClient getOkHttpClient() {
        return new OkHttpClient();
    }

    @Override
    public void run(String... args) {
    }

    @Override
    public void run(ApplicationArguments args) {
        Optional<Users> userOp = userRepository.findByEmail("test@gmail.com");
        if (userOp.isEmpty()) {
            Users user = new Users();
            user.setEmail("test@gmail.com");
            user.setName("Test");
            user.setStatus("PENDING");
            user.setPassword(encoder.encode("123456"));
            user.setRole(Role.ROLE_USER);
            userRepository.save(user);
        }
    }
}
