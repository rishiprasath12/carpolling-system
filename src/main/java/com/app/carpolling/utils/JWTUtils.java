package com.app.carpolling.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JWTUtils {
  
  private static final Logger logger = LoggerFactory.getLogger(JWTUtils.class);
  
  // keep this in vault
  private static final String SECRET = "rishi prasath is a good boy and very handsome boy";
  private static final Key secretKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

  public String generateToken(String phoneNumber) {
    if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
      logger.error("Cannot generate token: phoneNumber is null or empty");
      throw new IllegalArgumentException("Phone number cannot be null or empty");
    }
    
    logger.debug("Generating JWT token for phoneNumber: {}", phoneNumber);
    try {
      String token = Jwts.builder()
          .setSubject(phoneNumber)
          .setIssuedAt(new Date(System.currentTimeMillis()))
          .signWith(secretKey, SignatureAlgorithm.HS256)
          .compact();
      logger.info("Successfully generated JWT token for phoneNumber: {}", phoneNumber);
      return token;
    } catch (Exception e) {
      logger.error("Error generating JWT token for phoneNumber: {}, Error: {}", phoneNumber, e.getMessage());
      throw e;
    }
  }

  public String extractPhoneNumber(String token) {
    if (token == null || token.trim().isEmpty()) {
      logger.error("Cannot extract phoneNumber: token is null or empty");
      throw new IllegalArgumentException("Token cannot be null or empty");
    }
    
    logger.debug("Extracting phoneNumber from token");
    try {
      String phoneNumber = Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token)
          .getBody()
          .getSubject();
      
      if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
        logger.error("Extracted phoneNumber from token is null or empty");
        throw new IllegalStateException("Token subject (phoneNumber) is null or empty");
      }
      
      logger.debug("Successfully extracted phoneNumber from token: {}", phoneNumber);
      return phoneNumber;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
      throw e;
    } catch (MalformedJwtException e) {
      logger.error("Malformed JWT token: {}", e.getMessage());
      throw e;
    } catch (UnsupportedJwtException e) {
      logger.error("Unsupported JWT token: {}", e.getMessage());
      throw e;
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty or invalid: {}", e.getMessage());
      throw e;
    } catch (JwtException e) {
      logger.error("JWT token validation failed: {}", e.getMessage());
      throw e;
    }
  }

  public boolean validateToken(String token) {
    // Handle null or empty token
    if (token == null) {
      logger.warn("Token validation failed: token is null");
      return false;
    }
    
    if (token.trim().isEmpty()) {
      logger.warn("Token validation failed: token is empty");
      return false;
    }
    
    // Handle token that's too short to be valid
    if (token.length() < 10) {
      logger.warn("Token validation failed: token is too short (length: {})", token.length());
      return false;
    }
    
    logger.debug("Validating JWT token");
    try {
      extractPhoneNumber(token);
      logger.info("JWT token validation successful");
      return true;
    } catch (SignatureException e) {
      logger.error("Token validation failed: Invalid signature - {}", e.getMessage());
      return false;
    } catch (MalformedJwtException e) {
      logger.error("Token validation failed: Malformed token - {}", e.getMessage());
      return false;
    } catch (UnsupportedJwtException e) {
      logger.error("Token validation failed: Unsupported token - {}", e.getMessage());
      return false;
    } catch (IllegalArgumentException e) {
      logger.error("Token validation failed: Invalid token argument - {}", e.getMessage());
      return false;
    } catch (IllegalStateException e) {
      logger.error("Token validation failed: Invalid token state - {}", e.getMessage());
      return false;
    } catch (JwtException e) {
      logger.error("Token validation failed: JWT exception - {}", e.getMessage());
      return false;
    } catch (Exception e) {
      logger.error("Token validation failed: Unexpected error - {}", e.getMessage(), e);
      return false;
    }
  }

}
