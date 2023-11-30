package com.example.test.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
	private byte[] privateKey;

	@PostConstruct
	public void init() {

		try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("key/PrivateKey.txt")) {
            privateKey =resourceAsStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String generateToken(String username) {
		return generateToken(new HashMap<>(), username);
	}

	public String generateToken(
			Map<String, Object> extraClaims,
			String username) {
		LocalDateTime now = LocalDateTime.now();
		return Jwts
				.builder()
				.setClaims(extraClaims)
				.setSubject(username)
				.setExpiration(Date.from(now.plusHours(SecurityConstants.EXPIRATION_TIME_HOURS)
						.atZone(ZoneId.systemDefault()).toInstant()))
				.setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	private Key getSignInKey() {

		return Keys.hmacShaKeyFor(privateKey);
	}
}
