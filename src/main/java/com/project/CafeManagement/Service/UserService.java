package com.project.CafeManagement.Service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.CafeManagement.wrapper.userWrapper;

@Service
public interface UserService {

	// Method to handle user sign-up
	ResponseEntity<String> signUp(Map<String, String> requestMap);

	ResponseEntity<String> signin(Map<String, String> requestMap);

	List<userWrapper> getAllUsers();

	ResponseEntity<String> updateUser(Map<String, String> requestMap);

}
