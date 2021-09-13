	package com.api.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.model.User;
import com.api.model.UserDetails;
import com.api.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("")
	@ResponseStatus(HttpStatus.OK)
	public List<UserDetails> getList(
			@RequestParam(defaultValue="1") Integer page, 
			@RequestParam(defaultValue="10") Integer limit) {
		
		List<User> users = userService.getList(page-1, limit);
		List<UserDetails> usersDetails = new ArrayList<>();
		for (User u : users) {
			UserDetails userDetails = new UserDetails();
			userDetails.setId(u.getId());
			userDetails.setUsername(u.getUsername());
			userDetails.setRole(u.getRole());
			usersDetails.add(userDetails);
		}
		
		return usersDetails;
	}
	
	@GetMapping("/{id:\\d+}")
	public ResponseEntity<?> getUserById(@PathVariable("id") Integer id) {
		User user = userService.getOneById(id);
		JSONObject response = new JSONObject();

		if(user == null) {
			response.put("Error", "User not found");
			return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
		}
		
		UserDetails userDetails = new UserDetails();
		userDetails.setId(user.getId());
		userDetails.setUsername(user.getUsername());
		userDetails.setRole(user.getRole());
		userDetails.setAddresses(user.getAddresses());
		
		return new ResponseEntity<UserDetails>(userDetails, HttpStatus.OK);
	}
	
	@GetMapping("/{username:[A-z]+}")
	public ResponseEntity<?> getUserByUsername(@PathVariable("username") String username) {
		User user = userService.getOneByUsername(username);
		JSONObject response = new JSONObject();

		if(user == null) {
			response.put("Error", "User not found");
			return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
		}
		
		UserDetails userDetails = new UserDetails();
		userDetails.setId(user.getId());
		userDetails.setUsername(user.getUsername());
		userDetails.setRole(user.getRole());
		userDetails.setAddresses(user.getAddresses());
		
		return new ResponseEntity<UserDetails>(userDetails, HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody User user, Principal principal) {
		User currentUser = userService.getOneByUsername(principal.getName());
		JSONObject response = new JSONObject();
		Boolean isUser = currentUser.getRole() == User.UserRole.ROLE_USER;
		User userFound = userService.getOneById(id);
		User userExist = userService.getOneByUsername(user.getUsername());
		String usernameExist = "";
		if (userExist != null) {
			usernameExist = userExist.getUsername();
		}
		
		if(userFound == null) {
			response.put("Error", "User not found");
			return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
		}
		
		// If user without admin privilege try to manipulate other user data
		if(id != currentUser.getId() && isUser) {
			response.put("Error", "You can't edit another user profile");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}
		
		// If user without admin privilege try to elevate himself to admin 
		if (user.getRole() == User.UserRole.ROLE_ADMIN && isUser) {
			response.put("Error", "Only admins can edit roles");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}

		if (usernameExist != "" && usernameExist != userFound.getUsername()) {
			response.put("Error", "User already exists");
			return new ResponseEntity<JSONObject>(response, HttpStatus.CONFLICT);
		}
		
		user = userService.update(id, user);
		UserDetails userDetails = new UserDetails();
		userDetails.setId(user.getId());
		userDetails.setUsername(user.getUsername());
		userDetails.setRole(user.getRole());

		return new ResponseEntity<UserDetails>(userDetails, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<JSONObject> delete(@PathVariable("id") Integer id, Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		Boolean success = false;
		JSONObject response = new JSONObject();
		
		if(id != user.getId() && user.getRole() != User.UserRole.ROLE_ADMIN) {
			response.put("success", success);
			response.put("Forbidden", "You cannot access this ressource");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}
		
		success = userService.delete(id);
		response.put("success", success);
		if (success) {
			return new ResponseEntity<JSONObject>(response, HttpStatus.OK);
		}
		
		response.put("Error", "User not found");
		return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
	}
}
