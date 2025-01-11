package com.project.CafeManagement.ServiceImple;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.CafeManagement.Constants.CafeConstants;
import com.project.CafeManagement.JWT.CustomrUserDetailsService;
import com.project.CafeManagement.JWT.JwtRequestFilter;
import com.project.CafeManagement.JWT.JwtUtil;
import com.project.CafeManagement.POJO.User;
import com.project.CafeManagement.Service.UserService;
import com.project.CafeManagement.dao.UserDao;
import com.project.CafeManagement.utils.CafeUtils;
import com.project.CafeManagement.utils.EmailUtils;
import com.project.CafeManagement.wrapper.userWrapper;

import ch.qos.logback.classic.Logger;

@Service
public class UserServiceImple implements UserService {

//	private static final Logger log = LoggerFactory.getLogger(UserServiceImple.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomrUserDetailsService customrUserDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	JwtRequestFilter jwtRequestFilter;

	@Autowired
	EmailUtils emailUtils;

	User user = new User();

	@Override
	public ResponseEntity<String> signin(Map<String, String> requestMap) {
		Logger log;
		// log.info("User attempting to login with email: {}", requestMap.get("email"));
		try {
			User user = userDao.findByEmailId(requestMap.get("email"));
			if (user == null) {
				return CafeUtils.getResponseEntity("User not found", HttpStatus.UNAUTHORIZED);
			}
			if (passwordEncoder.matches(requestMap.get("password"), user.getPassword())) {
				if (user.getStatus().equalsIgnoreCase("true")) {
					String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
					return new ResponseEntity<>("{\"token\":\"" + token + "\"}", HttpStatus.OK);
				} else {
					return CafeUtils.getResponseEntity("User account is not activated", HttpStatus.UNAUTHORIZED);
				}
			} else {
				return CafeUtils.getResponseEntity("Invalid credentials", HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			// log.error("Login failed for email: {} due to: {}", requestMap.get("email"),
			// e.getMessage());
			return CafeUtils.getResponseEntity("An error occurred during login", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		try {
			// log.info("Attempting to sign up user with email: {}",
			// requestMap.get("email"));
			if (validateSignUpMap(requestMap)) {
				User user = userDao.findByEmailId(requestMap.get("email"));
				if (Objects.isNull(user)) {
					User newUser = getUserFromMap(requestMap);
					newUser.setPassword(passwordEncoder.encode(requestMap.get("password")));
					userDao.save(newUser);
					return CafeUtils.getResponseEntity("User signed up successfully!", HttpStatus.OK);
				} else {
					return CafeUtils.getResponseEntity("Email already exists", HttpStatus.BAD_REQUEST);
				}
			} else {
				return CafeUtils.getResponseEntity("Invalid data", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception ex) {
			// log.error("An error occurred while signing up: ", ex);
			return CafeUtils.getResponseEntity("An error occurred while signing up", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private boolean validateSignUpMap(Map<String, String> requestMap) {
		return requestMap.containsKey("name") && requestMap.containsKey("contactnumber")
				&& requestMap.containsKey("email") && requestMap.containsKey("password");
	}

	private User getUserFromMap(Map<String, String> requestMap) {
		User user = new User();
		user.setName(requestMap.get("name"));
		user.setContactnumber(requestMap.get("contactnumber"));
		user.setEmail(requestMap.get("email"));
		user.setPassword(requestMap.get("password"));
		user.setStatus("false");
		user.setRole("User");
		return user;
	}

	@Override
	public List<userWrapper> getAllUsers() {
		try {
			if (jwtRequestFilter.isAdmin()) {
				List<User> users = userDao.findAll();
				return users.stream().map(user -> new userWrapper(user.getId(), user.getName(), user.getContactnumber(),
						user.getEmail(), user.getStatus(), user.getRole())).collect(Collectors.toList());
			} else {
				return new ArrayList<>();
			}
		} catch (Exception e) {
			// log.error("Error occurred while fetching users: {}", e.getMessage());
			return new ArrayList<>();
		}
	}

	@Override
	public ResponseEntity<String> updateUser(Map<String, String> requestMap) {
		try {
			if (jwtRequestFilter.isAdmin()) {
				Optional<User> optional = userDao.findById(Integer.parseInt(requestMap.get("id")));
				if (optional.isPresent()) {
					userDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
					sendMailToAllAdmin(Boolean.parseBoolean(requestMap.get("status")), optional.get().getEmail(),
							userDao.getAllAdmin());
					return CafeUtils.getResponseEntity("User status updated successfully", HttpStatus.OK);
				} else {
					return CafeUtils.getResponseEntity("User does not exist", HttpStatus.OK);
				}
			} else {
				return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA_, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			// log.error("Error occurred while updating user: {}", e.getMessage());
			return CafeUtils.getResponseEntity("An error occurred while updating user",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private void sendMailToAllAdmin(boolean status, String email, List<String> allAdmin) {
		allAdmin.remove(jwtRequestFilter.getCurrentUser());
		if (status) {
			emailUtils.sendSimpleMessage(jwtRequestFilter.getCurrentUser(), "Account Approved",
					"USER: " + email + " is Approved by Admin: " + jwtRequestFilter.getCurrentUser(), allAdmin);
		} else {
			emailUtils.sendSimpleMessage(jwtRequestFilter.getCurrentUser(), "Account Disabled",
					"USER: " + email + " is Disabled by Admin: " + jwtRequestFilter.getCurrentUser(), allAdmin);
		}
	}

}
