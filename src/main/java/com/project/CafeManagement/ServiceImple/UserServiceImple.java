package com.project.CafeManagement.ServiceImple;

import java.util.Map;
import java.util.Objects;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import com.project.CafeManagement.POJO.User;
import com.project.CafeManagement.Service.UserService;
import com.project.CafeManagement.dao.UserDao;
import com.project.CafeManagement.utils.CafeUtils;

import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImple implements UserService {

	private static final Logger log = (Logger) LoggerFactory.getLogger(UserServiceImple.class);

	@Autowired
	private UserDao userDao;
	@Autowired
	AuthenticationManager authenticationManager;

	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		try {
			log.info("Attempting to sign up user with email: {}", requestMap.get("email"));

			if (validateSignUpMap(requestMap)) {
				User user = userDao.findByEmailId(requestMap.get("email"));

				if (Objects.isNull(user)) {
					userDao.save(getUserFromMap(requestMap));
					log.info("User signed up successfully: {}", requestMap.get("email"));
					return CafeUtils.getResponseEntity("User signed up successfully!", HttpStatus.OK);
				} else {
					log.warn("Email already exists: {}", requestMap.get("email"));
					return CafeUtils.getResponseEntity("Email already exists", HttpStatus.BAD_REQUEST);
				}
			} else {
				log.warn("Invalid data provided during sign-up: {}", requestMap);
				return CafeUtils.getResponseEntity("Invalid data", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception ex) {
			log.error("An error occurred while signing up: ", ex);
			return CafeUtils.getResponseEntity("An error occurred while signing up", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Method to validate sign-up data
	private boolean validateSignUpMap(Map<String, String> requestMap) {
		boolean isValid = requestMap.containsKey("name") && requestMap.containsKey("contactnumber")
				&& requestMap.containsKey("email") && requestMap.containsKey("password");
		log.info("Sign-up data validation result: {}", isValid);
		return isValid;
	}

	// Method to map request data to a User entity
	private User getUserFromMap(Map<String, String> requestMap) {
		User user = new User();
		user.setName(requestMap.get("name"));
		user.setContactnumber(requestMap.get("contactnumber"));
		user.setEmail(requestMap.get("email"));
		user.setPassword(requestMap.get("password"));
		user.setStatus("false");
		user.setRole("User");
		log.info("Mapped request data to User entity: {}", user);
		return user;
	}

	// Login method to authenticate user credentials
//    public boolean login(String username, String password) {
//        try {
//            // Authenticate using AuthenticationManager
//            Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(username, password)
//            );
//
//            // If authentication is successful, return true
//            return authentication.isAuthenticated();
//
//        } catch (AuthenticationException e) {
//            // Handle authentication failure
//            return false;
//        }
//    }
}
