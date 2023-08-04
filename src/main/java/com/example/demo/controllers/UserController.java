package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.repository.UserRepo;

import java.util.List;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
// import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/user")
public class UserController {
	@Autowired private UserRepo userRepo;

	@GetMapping("/info")
	public User getUserDetails() {
		String username = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userRepo.findByUsername(username).get();
	}

	@GetMapping("/all")
	public List<User> getAllUsers() throws Exception{
		List<User> users = new ArrayList<User>();
		if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
		{
			userRepo.findAll().forEach(user -> users.add(user));
			return users;
		}
		throw new Exception("Authorization Required");
	}
}
