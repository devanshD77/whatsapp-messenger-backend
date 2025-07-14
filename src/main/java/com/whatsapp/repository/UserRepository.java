package com.whatsapp.repository;

import com.whatsapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.username LIKE %:searchTerm%")
    List<User> findByUsernameContaining(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT u FROM User u WHERE u.id != :userId")
    List<User> findAllExceptUser(@Param("userId") Long userId);
} 