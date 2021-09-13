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

import com.api.model.Question;
import com.api.model.User;
import com.api.service.QuestionService;
import com.api.service.UserService;

@RestController
@RequestMapping("/question")
public class QuestionController {
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("")
	@ResponseStatus(HttpStatus.OK)
	public List<Question> getList(
			@RequestParam(defaultValue="1") Integer page,
			@RequestParam(defaultValue="10") Integer limit,
			Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		
		if (user.getRole() == User.UserRole.ROLE_ADMIN)
			return questionService.getList(page - 1, limit);
		
		return null;
	}
		
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getQuestionById(@PathVariable("id") Integer id, Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		Question question = null;
		JSONObject response = new JSONObject();
				
		if (user.getRole() == User.UserRole.ROLE_ADMIN)
			question = questionService.getOneById(id);
		else {
			response.put("Forbidden", "You cannot access this ressource");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}
		
		if(question == null) {
			response.put("Error", "Question Not Found");
			return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Question>(question, HttpStatus.OK);
	}
	
	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@RequestBody Question question, Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		JSONObject response = new JSONObject();
		
		if (user.getRole() != User.UserRole.ROLE_ADMIN) {
			response.put("Forbidden", "You cannot access this ressource");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}
		
		question = questionService.create(question);
		return new ResponseEntity<Question>(question, HttpStatus.CREATED);

	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Question question, Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		JSONObject response = new JSONObject();
		
		Question questionFound = questionService.getOneById(id);
		
		if (user.getRole() != User.UserRole.ROLE_ADMIN) {
			response.put("Forbidden", "You cannot access this ressource");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}
				
		if(questionFound == null) {
			response.put("Error", "Question Not Found");
			return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
		}
		
		question = questionService.update(id, question);
		return new ResponseEntity<>(question, HttpStatus.OK);
		
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<JSONObject> delete(@PathVariable("id") Integer id, Principal principal) {
		Boolean success = false;
		User user = userService.getOneByUsername(principal.getName());
		JSONObject response = new JSONObject();
		
		Question questionFound = questionService.getOneById(id);
		
		if (user.getRole() != User.UserRole.ROLE_ADMIN) {
			response.put("Forbidden", "You cannot access this ressource");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}
		
		success = questionService.delete(id);
		
		response.put("success", success);
		
		if (success) {
			return new ResponseEntity<JSONObject>(response, HttpStatus.OK);
		}
		response.put("Error", "Question Not Found");
		return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
	}
}
