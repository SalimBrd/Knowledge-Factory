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
import com.api.model.User;
import com.api.service.CourseService;
import com.api.service.UserService;

@RestController
@RequestMapping("/course")
public class CourseController {
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("")
	@ResponseStatus(HttpStatus.OK)
	public List<Course> getList(
			@RequestParam(defaultValue="1") Integer page, 
			@RequestParam(defaultValue="10") Integer limit, 
			Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		
		if (user.getRole() == User.UserRole.ROLE_ADMIN)
			return courseService.getList(page - 1, limit);
		
		return null;
	}
	
	@GetMapping("/default")
	@ResponseStatus(HttpStatus.OK)
	public List<Course> getDefaultCourseList(
			@RequestParam(defaultValue="1") Integer page, 
			@RequestParam(defaultValue="10") Integer limit, Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		
		return courseService.getCourseByType(false, page - 1, limit);
	}
	
	@GetMapping("premium")
	@ResponseStatus(HttpStatus.OK)
	public List<Course> getPremiumCourseList(
			@RequestParam(defaultValue="1") Integer page, 
			@RequestParam(defaultValue="10") Integer limit, Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		
		return courseService.getCourseByType(true, page - 1, limit);
	}
		
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getCourseById(@PathVariable("id") Integer id) {
		Course course = courseService.getOneById(id);
		JSONObject response = new JSONObject();
		
		if(course == null) {
			response.put("Error", "Course Not Found");
			return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Course>(course, HttpStatus.OK);
	}
	
	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@RequestBody Course course, Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		JSONObject response = new JSONObject();
		
		if (user.getRole() != User.UserRole.ROLE_ADMIN) {
			response.put("Forbidden", "You cannot access this ressource");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}
		
		course = courseService.create(course);
		return new ResponseEntity<Course>(course, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Course course, Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		JSONObject response = new JSONObject();
		
		if (user.getRole() != User.UserRole.ROLE_ADMIN) {
			response.put("Forbidden", "You cannot access this ressource");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}
		
		Course courseFound = courseService.getOneById(id);
		
		if(courseFound == null) {
			response.put("Error", "Course Not Found");
			return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
		}
		
		course = courseService.update(id, course);
		return new ResponseEntity<>(course, HttpStatus.OK);
		
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<JSONObject> delete(@PathVariable("id") Integer id, Principal principal) {
		Boolean success = false;
		User user = userService.getOneByUsername(principal.getName());
		JSONObject response = new JSONObject();
		
		Course courseFound = courseService.getOneById(id);
		
		if (user.getRole() != User.UserRole.ROLE_ADMIN) {
			response.put("Forbidden", "You cannot access this ressource");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}
		
		success = courseService.delete(id);
		
		response.put("success", success);
		
		if (success) {
			return new ResponseEntity<JSONObject>(response, HttpStatus.OK);
		}
		
		response.put("Error", "Course Not Found");
		return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
	}
}
