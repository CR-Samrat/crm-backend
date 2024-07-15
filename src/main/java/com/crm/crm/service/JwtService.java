package com.crm.crm.service;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateTokenForUser(String username);
    String extractUsernameFromToken(String token);
    Date extractExpirationFromToken(String token);
    Boolean isTokenExpired(String token);
    Boolean validateToken(String token, String username);
}
