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

import com.api.model.Course;
import com.api.model.Diploma;
import com.api.model.User;
import com.api.service.CourseService;
import com.api.service.DiplomaService;
import com.api.service.UserService;

@RestController
@RequestMapping("/diploma")
public class DiplomaController {
	
	@Autowired
	private DiplomaService diplomaService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private CourseService courseService;
	
	@GetMapping("")
	@ResponseStatus(HttpStatus.OK)
	public List<Diploma> getList(
			@RequestParam(defaultValue="1") Integer page, 
			@RequestParam(defaultValue="10") Integer limit, Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		
		return diplomaService.getListForUser(page - 1, limit, user.getId());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getDiplomaById(@PathVariable("id") Integer id, Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		Diploma diploma = null;
		JSONObject response = new JSONObject();
		
		if (user.getRole() != User.UserRole.ROLE_ADMIN)
			diploma = diplomaService.getOneByIdForUser(id, user.getId());
		else
			diploma = diplomaService.getOneById(id);
		
		if(diploma == null) {
			response.put("Error", "Diploma Not Found");
			return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Diploma>(diploma, HttpStatus.OK);
	}
	
	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@RequestBody Diploma diploma, Principal principal) {
		JSONObject response = new JSONObject();
		User user = userService.getOneByUsername(principal.getName());
		User userFound = userService.getOneById(diploma.getUser().getId());
		Course courseFound = courseService.getOneById(diploma.getCourse().getId());
		
		if(userFound == null || courseFound == null) {
			response.put("Bad Request", "User or course does not exist");
			return new ResponseEntity<JSONObject>(response, HttpStatus.BAD_REQUEST);
		}
		
		if (user.getId() != userFound.getId()) {
			response.put("Forbidden", "You cannot access this ressource");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}
		
		Diploma diplomaFound = diplomaService.getOneByUserAndCourseId(diploma.getUser().getId(), diploma.getCourse().getId());
		
		if (diplomaFound != null) {
			response.put("Conflict", "Diploma already exists");
			return new ResponseEntity<JSONObject>(response, HttpStatus.CONFLICT);
		}
		
		diploma = diplomaService.create(diploma);
		return new ResponseEntity<Diploma>(diploma, HttpStatus.CREATED);

	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<JSONObject> delete(@PathVariable("id") Integer id, Principal principal) {
		Boolean success = false;
		User user = userService.getOneByUsername(principal.getName());
		JSONObject response = new JSONObject();
				
		if (user.getRole() != User.UserRole.ROLE_ADMIN) {
			response.put("Forbidden", "You cannot access this ressource");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}
		
		success = diplomaService.delete(id);
		
		response.put("success", success);
		
		if (success) {
			return new ResponseEntity<JSONObject>(response, HttpStatus.OK);
		}
		response.put("Error", "Diploma Not Found");
		return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
	}
}
