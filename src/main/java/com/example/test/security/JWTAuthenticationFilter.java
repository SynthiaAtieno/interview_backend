package com.example.test.security;

import com.example.test.dto.LoginDto;
import com.example.test.entities.Users;
import com.example.test.response.GenericResponse;
import com.example.test.response.LoginResponse;
import com.example.test.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        String message = "";
        try {
            LoginDto creds = new LoginDto();
            String username = creds.getUsername();
            String password = creds.getPassword();

            var userOp = userService.findByEmail(username);
            if(userOp.isEmpty())message = "Username incorrect please check and retry";
            else{
                message = "Password incorrect";
            }

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>()));
        } catch (Exception e) {
            log.warn("Authentication failed for hostname {} Ip address: {}", req.getLocalName(), req.getLocalAddr());
            res.setStatus(401);
            try {
                PrintWriter out = res.getWriter();
                res.setContentType("application/json");
                res.setCharacterEncoding("UTF-8");
                out.print(new ObjectMapper().writeValueAsString(new GenericResponse(res.getStatus(), message)));
                out.flush();
            } catch (Exception ignored) {}


        }
        return null;
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException {

        log.warn("Authentication Successful User {} Ip address: {}", ((User) auth.getPrincipal()).getUsername(), req.getRemoteAddr());
        String username = ((User) auth.getPrincipal()).getUsername();

        Users userOp = userService.findByEmail(username).get();
        String token = jwtService.generateToken(username);

        var loginResponse = new LoginResponse(token, userOp.getRole(), res.getStatus(), "Login Successful", userOp);

        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        out.print(new ObjectMapper().writeValueAsString(loginResponse));
        out.flush();

    }
}
