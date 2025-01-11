package com.project.CafeManagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.CafeManagement.POJO.User;
import com.project.CafeManagement.wrapper.userWrapper;

public interface UserDao extends JpaRepository<User, Integer> {

	// Method to find a user by email
	User findByEmailId(@Param("email") String email);

	@Query(name = "user.getAllUsers")
	List<userWrapper> getAllUsers();

	Integer updateStatus(@Param("Status") String status, @Param("id") Integer id);

	@Query(name = "user.getAllUsers")
	List<String> getAllAdmin();

}
