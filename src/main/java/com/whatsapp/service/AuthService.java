package com.whatsapp.service;

import com.whatsapp.dto.AuthRequest;
import com.whatsapp.dto.AuthResponse;
import com.whatsapp.dto.UserRegistrationRequest;

public interface AuthService {
    AuthResponse login(AuthRequest request);
    AuthResponse register(UserRegistrationRequest request);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
} 