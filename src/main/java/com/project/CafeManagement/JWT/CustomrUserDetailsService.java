package com.project.CafeManagement.JWT;

import java.util.Collections;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.CafeManagement.POJO.User;
import com.project.CafeManagement.dao.UserDao;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomrUserDetailsService implements UserDetailsService {

	@Autowired
	private UserDao userDao;

	private User userDetail;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Find the user from the database using the UserDao
		log.info("inside loadUserByUsername {}", username);
		userDetail = userDao.findByEmailId(username);

		if (!Objects.isNull(userDetail)) {
			// Return UserDetails implementation
			return new org.springframework.security.core.userdetails.User(userDetail.getEmail(),
					userDetail.getPassword(),
					Collections.singletonList(new SimpleGrantedAuthority(userDetail.getRole())));
		} else {
			throw new UsernameNotFoundException("User not found with email: " + username);
		}
	}

//	public User getUserDetails() {
//		return userDetail;
//	}

	public User getUserDetails() {
		// TODO Auto-generated method stub
		return userDetail;
	}
}
