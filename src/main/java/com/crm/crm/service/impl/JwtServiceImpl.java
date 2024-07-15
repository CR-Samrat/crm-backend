package com.crm.crm.service.impl;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.crm.crm.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService{

    private static final String SECRET = "MRzI9jKHW3aK0Gz1a4x+KQcU+/EEZOuoHg4ucgAU72Fn+BzTNO5KdnpkzZbzvkW7";

    @Override
    public String generateTokenForUser(String username) {
        Map<String, Object> claims = new HashMap<>();

        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis()*1000*60*30))
                    .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String extractUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Date extractExpirationFromToken(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
    }

    @Override
    public Boolean isTokenExpired(String token) {
        return extractExpirationFromToken(token).before(new Date());
    }

    @Override
    public Boolean validateToken(String token, String username) {
        String usernameFromToken = extractUsernameFromToken(token);

        return (username.equals(usernameFromToken) && !isTokenExpired(token));
    }
    
}
