package com.whatsapp.service;

import com.whatsapp.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserDto createUser(UserDto userDto);
    
    UserDto updateUser(Long userId, UserDto userDto);
    
    Optional<UserDto> getUserById(Long userId);
    
    Optional<UserDto> getUserByUsername(String username);
    
    Page<UserDto> getAllUsers(Pageable pageable);
    
    List<UserDto> searchUsersByUsername(String searchTerm);
    
    boolean existsByUsername(String username);
    
    void deleteUser(Long userId);
} 