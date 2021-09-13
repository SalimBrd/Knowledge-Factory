package com.api.controller;

import java.security.Principal;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.model.Answer;
import com.api.model.User;
import com.api.service.AnswerService;
import com.api.service.UserService;

@RestController
@RequestMapping("/answer")
public class AnswerController {
	@Autowired
	private AnswerService answerService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("")
	@ResponseStatus(HttpStatus.OK)
	public List<Answer> getList(
			@RequestParam(defaultValue="1") Integer page, 
			@RequestParam(defaultValue="10") Integer limit,
			Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		
		if (user.getRole() == User.UserRole.ROLE_ADMIN)
			return answerService.getList(page - 1, limit);
		
		return null;
	}
		
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getAnswerById(@PathVariable("id") Integer id, Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		Answer answer = null;
		JSONObject response = new JSONObject();
				
		if (user.getRole() == User.UserRole.ROLE_ADMIN)
			answer = answerService.getOneById(id);
		else {
			response.put("Forbidden", "You cannot access this ressource");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}
		
		if(answer == null) {
			response.put("Error", "Answer Not Found");
			return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Answer>(answer, HttpStatus.OK);
	}
	
	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@RequestBody Answer answer, Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		JSONObject response = new JSONObject();
		
		if (user.getRole() != User.UserRole.ROLE_ADMIN) {
			response.put("Forbidden", "You cannot access this ressource");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}
		
		answer = answerService.create(answer);
		return new ResponseEntity<Answer>(answer, HttpStatus.CREATED);

	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Answer answer, Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		JSONObject response = new JSONObject();
		
		Answer answerFound = answerService.getOneById(id);
		
		if (user.getRole() != User.UserRole.ROLE_ADMIN) {
			response.put("Forbidden", "You cannot access this ressource");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}
				
		if(answerFound == null) {
			response.put("Error", "Answer Not Found");
			return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
		}
		
		answer = answerService.update(id, answer);
		return new ResponseEntity<>(answer, HttpStatus.OK);
		
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<JSONObject> delete(@PathVariable("id") Integer id, Principal principal) {
		Boolean success = false;
		User user = userService.getOneByUsername(principal.getName());
		JSONObject response = new JSONObject();
		
		Answer answerFound = answerService.getOneById(id);
		
		if (user.getRole() != User.UserRole.ROLE_ADMIN) {
			response.put("Forbidden", "You cannot access this ressource");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}
		
		success = answerService.delete(id);
		
		response.put("success", success);
		
		if (success) {
			return new ResponseEntity<JSONObject>(response, HttpStatus.OK);
		}
		response.put("Error", "Answer Not Found");
		return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
	}
}
