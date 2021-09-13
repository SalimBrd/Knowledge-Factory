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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.model.SurveyAnswer;
import com.api.model.SurveyChoice;
import com.api.model.User;
import com.api.service.SurveyAnswerService;
import com.api.service.SurveyChoiceService;
import com.api.service.UserService;

@RestController
@RequestMapping("/surveyAnswer")
public class SurveyAnswerController {
	@Autowired
	private SurveyAnswerService surveyAnswerService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private SurveyChoiceService surveyChoiceService;
	
	@GetMapping("")
	@ResponseStatus(HttpStatus.OK)
	public List<SurveyAnswer> getList(
			@RequestParam(defaultValue="1") Integer page, 
			@RequestParam(defaultValue="10") Integer limit,Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		
		if (user.getRole() != User.UserRole.ROLE_ADMIN)
			return surveyAnswerService.getListForUser(user.getId());
		else
			return surveyAnswerService.getList(page - 1, limit);
	}
	
	@GetMapping("/user")
	@ResponseStatus(HttpStatus.OK)
	public List<SurveyAnswer> getUserSurveyAnswers(Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		return surveyAnswerService.getListForUser(user.getId());
		
	}
		
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getSurveyAnswerById(@PathVariable("id") Integer id, Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		SurveyAnswer surveyAnswer = null;
		JSONObject response = new JSONObject();
		
		if (user.getRole() != User.UserRole.ROLE_ADMIN)
			surveyAnswer = surveyAnswerService.getOneByIdForUser(id, user.getId());
		else
			surveyAnswer = surveyAnswerService.getOneById(id);
		
		if(surveyAnswer == null) {
			response.put("Error", "SurveyAnswer Not Found");
			return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<SurveyAnswer>(surveyAnswer, HttpStatus.OK);
	}
	
	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@RequestBody SurveyAnswer surveyAnswer, Principal principal) {
		JSONObject response = new JSONObject();
		User user = userService.getOneByUsername(principal.getName());
		User userFound = userService.getOneById(surveyAnswer.getUser().getId());
		SurveyChoice surveyChoiceFound = surveyChoiceService.getOneById(surveyAnswer.getSurveyChoice().getId());
		
		if(userFound == null || surveyChoiceFound == null) {
			response.put("Bad Request", "User or surveyAnswer does not exist");
			return new ResponseEntity<JSONObject>(response, HttpStatus.BAD_REQUEST);
		}
		
		if (user.getId() != userFound.getId()) {
			response.put("Forbidden", "Cannot access this ressource");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}
		
		SurveyAnswer surveyAnswerFound = surveyAnswerService.getOneByUserAndSurveyChoiceId(surveyAnswer.getUser().getId(), surveyAnswer.getSurveyChoice().getId());
		
		if (surveyAnswerFound != null) {
			response.put("Conflict", "SurveyAnswer already exists");
			return new ResponseEntity<JSONObject>(response, HttpStatus.CONFLICT);
		}
		
		SurveyAnswer createdSurveyAnswer = surveyAnswerService.create(surveyAnswer);
		return new ResponseEntity<SurveyAnswer>(createdSurveyAnswer, HttpStatus.CREATED);

	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<JSONObject> delete(@PathVariable("id") Integer id, Principal principal) {
		Boolean success = false;
		User user = userService.getOneByUsername(principal.getName());
		SurveyAnswer surveyAnswer = surveyAnswerService.getOneById(id);
		JSONObject response = new JSONObject();
				
		if (user.getRole() != User.UserRole.ROLE_ADMIN && surveyAnswer.getId() != id) {
			response.put("Forbidden", "Cannot access this ressource");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}
		
		success = surveyAnswerService.delete(id);
		
		response.put("success", success);
		
		if (success) {
			return new ResponseEntity<JSONObject>(response, HttpStatus.OK);
		}
		response.put("Error", "SurveyAnswer Not Found");
		return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
	}
}
