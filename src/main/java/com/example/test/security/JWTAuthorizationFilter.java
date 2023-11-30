package com.example.test.security;

import com.example.test.response.GenericResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.AccessDeniedException;

public class JWTAuthorizationFilter extends OncePerRequestFilter {
    private UserDetailsService userDetailsService;
    private JwtService jwtService;
    private final ObjectMapper mapper = new ObjectMapper();

    public JWTAuthorizationFilter(UserDetailsService userDetailsService, JwtService jwtService) {

        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

        String token = request.getHeader(SecurityConstants.HEADER_STRING).replace(SecurityConstants.TOKEN_PREFIX, "").trim();
        String username = jwtService.extractUsername(token);
        UserDetails details = this.userDetailsService.loadUserByUsername(username);
       
        return new UsernamePasswordAuthenticationToken(username, "", details.getAuthorities());

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse res, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(SecurityConstants.HEADER_STRING);

        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            filterChain.doFilter(request, res);
            return;
        }
        try {
            UsernamePasswordAuthenticationToken authToken = this.getAuthentication(request);
            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(request, res);
        } catch (SignatureException e) {
            writeErrorResponse(res, new GenericResponse(731, "Invalid token signature"));
        } catch (ExpiredJwtException e) {
            writeErrorResponse(res, new GenericResponse(731, "Token expired please login again"));
        } catch (AccessDeniedException e) {
            writeErrorResponse(res, new GenericResponse(731, "Access denied"));
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            writeErrorResponse(res, new GenericResponse(731, "Malformed or unsupported token"));
        }

    }

    private void writeErrorResponse(HttpServletResponse res, GenericResponse response) {
        try {
            res.setStatus(response.getCode());
            PrintWriter out = res.getWriter();
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            out.print(mapper.writeValueAsString(response));
            out.flush();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
