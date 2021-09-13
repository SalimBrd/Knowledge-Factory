package com.api.controller;

import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
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

import com.api.model.Survey;
import com.api.model.User;
import com.api.service.SurveyService;
import com.api.service.UserService;

@RestController
@RequestMapping("/survey")
public class SurveyController {
	
	@Autowired
	private SurveyService surveyService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("")
	@ResponseStatus(HttpStatus.OK)
	public List<Survey> getList(
			@RequestParam(defaultValue="1") Integer page, 
			@RequestParam(defaultValue="10") Integer limit,
			Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		
		if (user.getRole() == User.UserRole.ROLE_ADMIN)
			return surveyService.getList(page - 1, limit);
		
		return null;
	}
	
	@GetMapping("/default")
	@ResponseStatus(HttpStatus.OK)
	public List<Survey> getCurrentSurveys() {
		return surveyService.getSurveysByType(false);
	}
	
	@GetMapping("/premium")
	@ResponseStatus(HttpStatus.OK)
	public List<Survey> getCurrentPremiumSurveys() {
		return surveyService.getSurveysByType(true);
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getSurveyById(@PathVariable("id") Integer id) {
		Survey survey = surveyService.getOneById(id);
		JSONObject response = new JSONObject();
		
		if(survey == null) {
			response.put("Error", "Survey Not Found");
			return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Survey>(survey, HttpStatus.OK);
	}
	
	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@RequestBody Survey survey, Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		JSONObject response = new JSONObject();
		
		if (user.getRole() != User.UserRole.ROLE_ADMIN) {
			response.put("Forbidden", "You cannot access this ressource");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}
		
		survey = surveyService.create(survey);
		return new ResponseEntity<Survey>(survey, HttpStatus.CREATED);

	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Survey survey, Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		JSONObject response = new JSONObject();
		
		if (user.getRole() != User.UserRole.ROLE_ADMIN) {
			response.put("Forbidden", "You cannot access this ressource");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}
		
		Survey surveyFound = surveyService.getOneById(id);
				
		if(surveyFound == null) {
			response.put("Error", "Survey Not Found");
			return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
		}
		
		survey = surveyService.update(id, survey);
		return new ResponseEntity<>(survey, HttpStatus.OK);
		
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<JSONObject> delete(@PathVariable("id") Integer id, Principal principal) {
		Boolean success = false;
		User user = userService.getOneByUsername(principal.getName());
		JSONObject response = new JSONObject();
		
		Survey surveyFound = surveyService.getOneById(id);
		
		if (user.getRole() != User.UserRole.ROLE_ADMIN) {
			response.put("Forbidden", "You cannot access this ressource");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}
		
		success = surveyService.delete(id);
		
		response.put("success", success);
		
		if (success) {
			return new ResponseEntity<JSONObject>(response, HttpStatus.OK);
		}
		response.put("Error", "Survey Not Found");
		return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
	}
}
