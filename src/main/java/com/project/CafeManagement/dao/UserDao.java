package com.project.CafeManagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.project.CafeManagement.POJO.User;

public interface UserDao extends JpaRepository<User, Integer> {
    
    // Method to find a user by email
    User findByEmailId(@Param("email") String email);
}
