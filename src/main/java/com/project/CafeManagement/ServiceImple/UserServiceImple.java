package com.project.CafeManagement.ServiceImple;

import java.util.Map;
import java.util.Objects;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.CafeManagement.JWT.CustomrUserDetailsService;
import com.project.CafeManagement.JWT.JwtUtil;
import com.project.CafeManagement.POJO.User;
import com.project.CafeManagement.Service.UserService;
import com.project.CafeManagement.dao.UserDao;
import com.project.CafeManagement.utils.CafeUtils;

import ch.qos.logback.classic.Logger;

@Service
public class UserServiceImple implements UserService {

	private static final Logger log = (Logger) LoggerFactory.getLogger(UserServiceImple.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomrUserDetailsService customrUserDetailsService;

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	private PasswordEncoder passwordEncoder;

//	@Override
//	public ResponseEntity<String> signin(Map<String, String> requestMap) {
//		log.info("User attempting to login with email: {}", requestMap.get("email"));
//		try {
//			// Fetch user from the database
//			User user = userDao.findByEmailId(requestMap.get("email"));
//
//			// Check if user exists
//			if (user == null) {
//				return CafeUtils.getResponseEntity("User not found", HttpStatus.UNAUTHORIZED);
//			}
//
//			// Compare the passwords
//			if (passwordEncoder.matches(requestMap.get("password"), user.getPassword())) {
//				if (user.getStatus().equalsIgnoreCase("true")) {
//					String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
//					log.info("User authenticated successfully with email: {}", user.getEmail());
//					return new ResponseEntity<>("{\"token\":\"" + token + "\"}", HttpStatus.OK);
//				} else {
//					log.warn("User account is not activated: {}", requestMap.get("email"));
//					return CafeUtils.getResponseEntity("User account is not activated", HttpStatus.UNAUTHORIZED);
//				}
//			} else {
//				log.warn("Password does not match for email: {}", requestMap.get("email"));
//				return CafeUtils.getResponseEntity("Invalid credentials", HttpStatus.UNAUTHORIZED);
//			}
//		} catch (Exception e) {
//			log.error("Login failed for email: {} due to: {}", requestMap.get("email"), e.getMessage());
//			return CafeUtils.getResponseEntity("An error occurred during login", HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}

	@Override
	public ResponseEntity<String> signin(Map<String, String> requestMap) {
		log.info("User attempting to login with email: {}", requestMap.get("email"));
		try {
			// Authenticate using AuthenticationManager
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password")));

			// Check if authentication was successful
			if (authentication.isAuthenticated()) {
				User user = customrUserDetailsService.getUserDetails();

				if (user.getStatus().equalsIgnoreCase("true")) {
					// Generate JWT token upon successful authentication
					String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

					log.info("User authenticated successfully with email: {}", user.getEmail());

					// Return the token in a JSON response
					return new ResponseEntity<>("{\"token\":\"" + token + "\"}", HttpStatus.OK);
				} else {
					log.warn("User account is not activated: {}", requestMap.get("email"));
					return CafeUtils.getResponseEntity("User account is not activated", HttpStatus.UNAUTHORIZED);
				}
			}
		} catch (Exception e) {
			// Handle authentication failure
			log.error("Login failed for email: {} due to: {}", requestMap.get("email"), e.getMessage());
			return CafeUtils.getResponseEntity("wait for admin approval", HttpStatus.UNAUTHORIZED);
		}

		return CafeUtils.getResponseEntity("Error during login", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		try {
			log.info("Attempting to sign up user with email: {}", requestMap.get("email"));

			if (validateSignUpMap(requestMap)) {
				User user = userDao.findByEmailId(requestMap.get("email"));

				if (Objects.isNull(user)) {
					User newUser = getUserFromMap(requestMap);
					// Hash the password before saving it
					String hashedPassword = passwordEncoder.encode(requestMap.get("password"));
					newUser.setPassword(hashedPassword);

					log.info("Hashed password for {}: {}", newUser.getEmail(), hashedPassword); // Log the hashed
																								// password

					userDao.save(newUser);
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

//	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
//		try {
//			log.info("Attempting to sign up user with email: {}", requestMap.get("email"));
//
//			if (validateSignUpMap(requestMap)) {
//				User user = userDao.findByEmailId(requestMap.get("email"));
//
//				String hashedPassword = passwordEncoder.encode(requestMap.get("password"));
//				user.setPassword(hashedPassword);
//				userDao.save(getUserFromMap(requestMap));
//
//				if (Objects.isNull(user)) {
//					userDao.save(getUserFromMap(requestMap));
//					log.info("User signed up successfully: {}", requestMap.get("email"));
//					return CafeUtils.getResponseEntity("User signed up successfully!", HttpStatus.OK);
//				} else {
//					log.warn("Email already exists: {}", requestMap.get("email"));
//					return CafeUtils.getResponseEntity("Email already exists", HttpStatus.BAD_REQUEST);
//				}
//			} else {
//				log.warn("Invalid data provided during sign-up: {}", requestMap);
//				return CafeUtils.getResponseEntity("Invalid data", HttpStatus.BAD_REQUEST);
//			}
//		} catch (Exception ex) {
//			log.error("An error occurred while signing up: ", ex);
//			return CafeUtils.getResponseEntity("An error occurred while signing up", HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}

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

}
