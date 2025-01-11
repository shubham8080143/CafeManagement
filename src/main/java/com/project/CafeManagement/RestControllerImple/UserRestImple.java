package com.project.CafeManagement.RestControllerImple;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.project.CafeManagement.RestController.userRestController;
import com.project.CafeManagement.Service.UserService;
import com.project.CafeManagement.utils.CafeUtils;
import com.project.CafeManagement.wrapper.userWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class UserRestImple implements userRestController {

	@Autowired
	private UserService userService;

	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		try {
			//log.info("Registering user with request data: {}", requestMap);
			return userService.signUp(requestMap);
		} catch (Exception ex) {
			//log.error("Error during sign-up: ", ex);
			return CafeUtils.getResponseEntity("An error occurred during sign-up", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<String> signin(Map<String, String> requestMap) {
		try {
			//log.info("Signing in user with request data: {}", requestMap);
			return userService.signin(requestMap);
		} catch (Exception e) {
			//log.error("Error during sign-in: ", e);
			return CafeUtils.getResponseEntity("An error occurred during sign-in. Please check your email or password.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<List<userWrapper>> getallUser() {
		try {
			//log.info("Fetching all users.");
			List<userWrapper> userList = userService.getAllUsers();
			return new ResponseEntity<>(userList, HttpStatus.OK);
		} catch (Exception e) {
			//log.error("Error fetching all users: ", e);
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<String> updateUser(Map<String, String> requestMap) {
		return userService.updateUser(requestMap);
	}
}
