package com.project.CafeManagement.RestControllerImple;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.project.CafeManagement.RestController.userRestController;
import com.project.CafeManagement.Service.UserService;
import com.project.CafeManagement.utils.CafeUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class UserRestImple implements userRestController {

	@Autowired
	private UserService userService; // Ensure the proper naming convention

	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		try {
			log.info("registered successfully {}");
			return userService.signUp(requestMap);
		} catch (Exception ex) {
			ex.printStackTrace();
			// Return an error response using CafeUtils
			return CafeUtils.getResponseEntity("An error occurred during sign-up", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<String> signin(Map<String, String> requestMap) {
		// TODO Auto-generated method stub
		try {
			log.info("Login Successfully {}");
			return userService.signin(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
			return CafeUtils.getResponseEntity("An error occured during sign please check your mail or password",
					HttpStatus.INTERNAL_SERVER_ERROR);
			// TODO: handle exception
		}
	}
}
