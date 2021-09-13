package com.api.controller;

import org.json.simple.JSONObject;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.api.config.JwtTokenUtil;
import com.api.model.JwtUserDetails;
import com.api.model.User;
import com.api.model.UserDetails;
import com.api.repositories.UserRepository;
import com.api.service.UserService;

@RestController
public class AuthenticationController {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@PostMapping(path = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> register(@RequestBody User user) {

		User userFound = userService.getOneByUsername(user.getUsername());
		
		JSONObject conflict = new JSONObject();
		JSONObject serverError = new JSONObject();
		String conflictMsg = "User already exists";
		String serverErrorMsg = "Parameter is missing";
		
		if (user.getPassword() == null || user.getUsername() == null) {
			serverError.put("Internal Server Error", serverErrorMsg);
			return new ResponseEntity<JSONObject>(serverError, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (userFound != null) {
			conflict.put("conflict", conflictMsg);
			return new ResponseEntity<JSONObject>(conflict, HttpStatus.CONFLICT);
		}
		
		try {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userRepository.save(user);
		} catch(Exception e) {
			serverError.put("Internal Server Error", e.getMessage());
			return new ResponseEntity<JSONObject>(serverError, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		UserDetails userDetails = new UserDetails();
		userDetails.setId(user.getId());
		userDetails.setUsername(user.getUsername());
		userDetails.setRole(user.getRole());
		
		return new ResponseEntity<>(userDetails, HttpStatus.CREATED);
	}
	
	@PostMapping(path = "/authenticate")
	public ResponseEntity<JSONObject> authenticate(@RequestBody User user) {

		JSONObject serverError = new JSONObject();
		
		User userFound = userService.getOneByUsername(user.getUsername());
		org.springframework.security.core.userdetails.UserDetails jwtUserDetails = new JwtUserDetails(userFound);
		try {
			authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		} catch (Exception e) {
			serverError.put("Internal Server Error", "Bad Credentials");
			return new ResponseEntity<JSONObject>(serverError, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		String token = jwtTokenUtil.generateToken(jwtUserDetails);
		
		JSONObject jwt = new JSONObject();
		jwt.put("token", token);
		
		return new ResponseEntity<JSONObject>(jwt, HttpStatus.OK);
		
	}
	
	@GetMapping(path= "/me")
	public ResponseEntity<?> me() {
		
		JSONObject unauthorized = new JSONObject();
		JSONObject serverError = new JSONObject();
		String unauthorizedMsg = "Unauthorized";
		String serverErrorMsg = "Internal Server Error";
		UserDetails userDetails = new UserDetails();
		Authentication auth = null;
		
		try {
			auth = SecurityContextHolder.getContext().getAuthentication();
		} catch (Exception e) {
			serverError.put(serverErrorMsg, e.getMessage());
			return new ResponseEntity<JSONObject>(serverError, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (auth == null) {
			unauthorized.put(unauthorizedMsg, unauthorizedMsg);
			return new ResponseEntity<JSONObject>(unauthorized, HttpStatus.UNAUTHORIZED);
		}
		
		User userLogged = userRepository.findByUsername(auth.getName()).get();
		userDetails.setId(userLogged.getId());
		userDetails.setUsername(userLogged.getUsername());
		userDetails.setRole(userLogged.getRole());
		userDetails.setAddresses(userLogged.getAddresses());
		
		return new ResponseEntity<>(userDetails, HttpStatus.OK);
		
	}
	
}
