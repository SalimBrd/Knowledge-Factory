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

import com.api.model.Questionnaire;
import com.api.model.User;
import com.api.service.QuestionnaireService;
import com.api.service.UserService;

@RestController
@RequestMapping("/questionnaire")
public class QuestionnaireController {
	@Autowired
	private QuestionnaireService questionnaireService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("")
	@ResponseStatus(HttpStatus.OK)
	public List<Questionnaire> getList(
			@RequestParam(defaultValue="1") Integer page,
			@RequestParam(defaultValue="10") Integer limit,
			Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		
		if (user.getRole() == User.UserRole.ROLE_ADMIN)
			return questionnaireService.getList(page - 1, limit);
		
		return null;
	}
		
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getQuestionnaireById(@PathVariable("id") Integer id, Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		Questionnaire questionnaire = null;
		JSONObject response = new JSONObject();
		
		questionnaire = questionnaireService.getOneById(id);
		
		if(questionnaire == null) {
			response.put("Error", "Questionnaire Not Found");
			return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Questionnaire>(questionnaire, HttpStatus.OK);
	}

	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@RequestBody Questionnaire questionnaire, Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		JSONObject response = new JSONObject();

		if (user.getRole() != User.UserRole.ROLE_ADMIN) {
			response.put("Forbidden", "You cannot access this ressource");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}
		
		questionnaire = questionnaireService.create(questionnaire);
		return new ResponseEntity<Questionnaire>(questionnaire, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Questionnaire questionnaire, Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		JSONObject response = new JSONObject();
		
		Questionnaire questionnaireFound = questionnaireService.getOneById(id);
		
		if (user.getRole() != User.UserRole.ROLE_ADMIN) {
			response.put("Forbidden", "You cannot access this ressource");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}
				
		if(questionnaireFound == null) {
			response.put("Error", "Questionnaire Not Found");
			return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
		}
		
		questionnaire = questionnaireService.update(id, questionnaire);
		return new ResponseEntity<>(questionnaire, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<JSONObject> delete(@PathVariable("id") Integer id, Principal principal) {
		Boolean success = false;
		User user = userService.getOneByUsername(principal.getName());
		JSONObject response = new JSONObject();
		
		Questionnaire questionnaireFound = questionnaireService.getOneById(id);
		
		if (user.getRole() != User.UserRole.ROLE_ADMIN) {
			response.put("Forbidden", "You cannot access this ressource");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}
		
		success = questionnaireService.delete(id);
		
		response.put("success", success);
		
		if (success) {
			return new ResponseEntity<JSONObject>(response, HttpStatus.OK);
		}
		response.put("Error", "Questionnaire Not Found");
		return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
	}
}
