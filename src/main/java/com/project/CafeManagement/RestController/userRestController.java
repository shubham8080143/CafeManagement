package com.project.CafeManagement.RestController;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public interface userRestController {

	@PostMapping("/auth/signup")
	public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String, String> requestMap);
//		System.out.println("registerd suuccsfully");

	public ResponseEntity<String> signin(@RequestBody(required = true) Map<String, String> requestMap);

}
