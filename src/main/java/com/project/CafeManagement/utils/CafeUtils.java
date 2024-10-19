package com.project.CafeManagement.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CafeUtils {

	// Private constructor to prevent instantiation
	private CafeUtils() {
		// No need for the super() call in a utility class
	}

	// Utility method to create a ResponseEntity with a message and status
	public static ResponseEntity<String> getResponseEntity(String message, HttpStatus status) {

		return new ResponseEntity<>(message, status); // Use diamond operator for cleaner code
	}
}
