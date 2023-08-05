package com.example.demo.controllers;

import java.util.Map;
import java.util.Optional;

import javax.swing.Spring;

import java.util.Collections;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.demo.models.User;
import com.example.demo.repository.UserRepo;
import com.example.demo.Responses.JwtToken;
import com.example.demo.Responses.Unauthorized;
import com.example.demo.models.LoginCredentials;
import com.example.demo.security.*;
import org.springframework.security.core.Authentication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:4200", "http://10.11.1.12:4200"})
public class AuthController {
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/register")
	public ResponseEntity<Map<String, Object>> registerHandler(@RequestBody User user) {
		String encodedPass = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPass);
		Optional<User> tempo = userRepo.findByUsername(user.getUsername());
		if (tempo.isPresent())
			return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("jwt-token", "already exist"));
		user = userRepo.save(user);
		String token = jwtUtil.generateToken(user.getUsername());
		return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("jwt-token", token));
	}

	@PostMapping("/login")
	@Operation(summary = "User Login", description = "Authenticate user with provided credentials and generate JWT TOKEN")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Success", response = JwtToken.class),
		@ApiResponse(code = 401, message = "Unauthorized", response = JwtToken.class),
		@ApiResponse(code = 403, message = "Invalid login credentials")
	})
	public ResponseEntity<Map<String, Object>> loginHandler(@RequestBody LoginCredentials body) {
		try {
			UsernamePasswordAuthenticationToken authInputToken =
				new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword());
			authManager.authenticate(authInputToken);
			String token = jwtUtil.generateToken(body.getUsername());
			Map<String, Object> test = Collections.singletonMap("Token", token);
			return ResponseEntity.status(HttpStatus.OK).body(test);
		} catch (AuthenticationException authExc) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("error", "Invalid login credentials"));
		}
	}
}

