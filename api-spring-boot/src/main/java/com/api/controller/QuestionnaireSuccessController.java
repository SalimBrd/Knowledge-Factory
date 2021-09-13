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

import com.api.model.Questionnaire;
import com.api.model.QuestionnaireSuccess;
import com.api.model.User;
import com.api.service.QuestionnaireService;
import com.api.service.QuestionnaireSuccessService;
import com.api.service.UserService;

@RestController
@RequestMapping("/questionnaireSuccess")
public class QuestionnaireSuccessController {
	
	@Autowired
	private QuestionnaireSuccessService questionnaireSuccessService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private QuestionnaireService questionnaireService;
	
	@GetMapping("")
	@ResponseStatus(HttpStatus.OK)
	public List<QuestionnaireSuccess> getList(
			@RequestParam(defaultValue="1") Integer page, 
			@RequestParam(defaultValue="10") Integer limit,Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		
		if (user.getRole() != User.UserRole.ROLE_ADMIN)
			return questionnaireSuccessService.getListForUser(page - 1, limit, user.getId());
		else
			return questionnaireSuccessService.getList(page - 1, limit);
	}
		
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getQuestionnaireSuccessById(@PathVariable("id") Integer id, Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		QuestionnaireSuccess questionnaireSuccess = null;
		JSONObject response = new JSONObject();
		
		if (user.getRole() != User.UserRole.ROLE_ADMIN)
			questionnaireSuccess = questionnaireSuccessService.getOneByIdForUser(id, user.getId());
		else
			questionnaireSuccess = questionnaireSuccessService.getOneById(id);
		
		if(questionnaireSuccess == null) {
			response.put("Error", "Questionnaire Success Not Found");
			return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<QuestionnaireSuccess>(questionnaireSuccess, HttpStatus.OK);
	}
	
	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@RequestBody QuestionnaireSuccess questionnaireSuccess, Principal principal) {
		JSONObject response = new JSONObject();
		User user = userService.getOneByUsername(principal.getName());
		User userFound = userService.getOneById(questionnaireSuccess.getUser().getId());
		Questionnaire questionnaireFound = questionnaireService.getOneById(questionnaireSuccess.getQuestionnaire().getId());
		
		if(userFound == null || questionnaireFound == null) {
			response.put("Bad Request", "User or questionnaire does not exist");
			return new ResponseEntity<JSONObject>(response, HttpStatus.BAD_REQUEST);
		}
		
		if (user.getId() != userFound.getId()) {
			response.put("Forbidden", "You cannot access this ressource");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}
		
		QuestionnaireSuccess questionnaireSuccessFound = 
				questionnaireSuccessService.getOneByUserAndQuestionnaireId(
						questionnaireSuccess.getUser().getId(), 
						questionnaireSuccess.getQuestionnaire().getId());
		
		if (questionnaireSuccessFound != null) {
			response.put("Conflict", "You already succeeded this questionnaire.");
			return new ResponseEntity<JSONObject>(response, HttpStatus.CONFLICT);
		}
		
		questionnaireSuccess = questionnaireSuccessService.create(questionnaireSuccess);
		return new ResponseEntity<QuestionnaireSuccess>(questionnaireSuccess, HttpStatus.CREATED);

	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<JSONObject> delete(@PathVariable("id") Integer id, Principal principal) {
		Boolean success = false;
		User user = userService.getOneByUsername(principal.getName());
		JSONObject response = new JSONObject();
				
		if (user.getRole() != User.UserRole.ROLE_ADMIN) {
			response.put("Error", "Unauthorized");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}
		
		success = questionnaireSuccessService.delete(id);
		
		response.put("success", success);
		
		if (success) {
			return new ResponseEntity<JSONObject>(response, HttpStatus.OK);
		}
		response.put("Error", "Questionnaire Success Not Found");
		return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
	}
}
