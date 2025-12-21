package com.app.carpolling.filters;

import com.app.carpolling.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JWTFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);
  private static final String BEARER_PREFIX = "Bearer ";
  private static final int BEARER_PREFIX_LENGTH = 7;

  @Autowired
  private JWTUtils jwtUtils;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String requestURI = request.getRequestURI();
    String authHeader = request.getHeader("Authorization");

    logger.debug("Processing request to URI: {} with Authorization header present: {}", 
        requestURI, authHeader != null);

    // Case 1: No Authorization header - proceed without authentication
    if (authHeader == null) {
      logger.debug("No Authorization header found for request to: {}", requestURI);
      filterChain.doFilter(request, response);
      return;
    }

    // Case 2: Authorization header is empty
    if (authHeader.trim().isEmpty()) {
      logger.warn("Empty Authorization header for request to: {}", requestURI);
      filterChain.doFilter(request, response);
      return;
    }

    // Case 3: Authorization header doesn't start with "Bearer "
    if (!authHeader.startsWith(BEARER_PREFIX)) {
      logger.warn("Invalid Authorization header format (missing 'Bearer ' prefix) for request to: {}. Header starts with: {}", 
          requestURI, authHeader.length() > 10 ? authHeader.substring(0, 10) : authHeader);
      filterChain.doFilter(request, response);
      return;
    }

    // Case 4: Authorization header is just "Bearer " with no token
    if (authHeader.length() <= BEARER_PREFIX_LENGTH) {
      logger.warn("Authorization header contains 'Bearer ' prefix but no token for request to: {}", requestURI);
      filterChain.doFilter(request, response);
      return;
    }

    // Extract token
    String token = authHeader.substring(BEARER_PREFIX_LENGTH);

    // Case 5: Token is empty or whitespace after "Bearer "
    if (token.trim().isEmpty()) {
      logger.warn("Empty token after 'Bearer ' prefix for request to: {}", requestURI);
      filterChain.doFilter(request, response);
      return;
    }

    logger.debug("Extracted JWT token from Authorization header for request to: {}", requestURI);

    // Case 6: Token validation
    try {
      if (jwtUtils.validateToken(token)) {
        logger.debug("JWT token validation successful for request to: {}", requestURI);
        
        String phoneNumber = jwtUtils.extractPhoneNumber(token);
        
        // Case 7: Phone number extraction validation
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
          logger.error("Failed to extract valid phoneNumber from token for request to: {}", requestURI);
          filterChain.doFilter(request, response);
          return;
        }
        
        logger.info("Successfully authenticated user with phoneNumber: {} for request to: {}", 
            phoneNumber, requestURI);
        
        var auth = new UsernamePasswordAuthenticationToken(phoneNumber, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);
        
      } else {
        logger.warn("JWT token validation failed for request to: {}", requestURI);
      }
    } catch (Exception e) {
      logger.error("Unexpected error during JWT token validation for request to: {}. Error: {}", 
          requestURI, e.getMessage(), e);
    }

    // Always proceed with the filter chain
    filterChain.doFilter(request, response);
  }

}
