package com.project.CafeManagement.Service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

	// Method to handle user sign-up
	ResponseEntity<String> signUp(Map<String, String> requestMap);

	ResponseEntity<String> signin(Map<String, String> requestMap);

}
