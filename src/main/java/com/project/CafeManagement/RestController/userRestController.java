package com.project.CafeManagement.RestController;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.CafeManagement.wrapper.userWrapper;

@RequestMapping
public interface userRestController {

	@PostMapping("/auth/signup")
	public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String, String> requestMap);

	@PostMapping("/auth/signin")
	public ResponseEntity<String> signin(@RequestBody(required = true) Map<String, String> requestMap);

	@GetMapping("/get")
	public ResponseEntity<List<userWrapper>> getallUser();

	@PutMapping("/update")
	public ResponseEntity<String> updateUser(@RequestBody Map<String, String> requestMap);

}
