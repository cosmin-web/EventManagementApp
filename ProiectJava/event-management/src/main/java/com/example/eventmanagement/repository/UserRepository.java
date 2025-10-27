package com.example.eventmanagement.repository;

import com.example.eventmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface UserRepository extends JpaRepository<User, Integer>
{
    Optional<User> findByEmail(String email);
    List<User> findByRol(User.Role rol);
    boolean existsByEmail(String email);
}