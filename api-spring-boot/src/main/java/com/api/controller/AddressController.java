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

import com.api.model.Address;
import com.api.model.User;
import com.api.model.User.UserRole;
import com.api.service.AddressService;
import com.api.service.UserService;

@RestController
@RequestMapping("/address")
public class AddressController {
	
	@Autowired
	private AddressService addressService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("")
	@ResponseStatus(HttpStatus.OK)
	public List<Address> getList(
			@RequestParam(defaultValue="1") Integer page, 
			@RequestParam(defaultValue="10") Integer limit,
			Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		
		if (user.getRole() != User.UserRole.ROLE_ADMIN)
			return addressService.getListForUser(page - 1, limit, user.getId());
		else
			return addressService.getList(page - 1, limit);
		}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getAddressById(@PathVariable("id") Integer id, Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		Address address;
		JSONObject response = new JSONObject();
				
		if (user.getRole() != User.UserRole.ROLE_ADMIN)
			address = addressService.getOneByIdForUser(id, user.getId());
		else
			address = addressService.getOneById(id);
		
		if(address == null) {
			response.put("Error", "Address Not Found");
			return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Address>(address, HttpStatus.OK);
	}
	
	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public Address create(@RequestBody Address address, Principal principal) {
		User user = null;
		if (address.getUser() == null) {
			user = userService.getOneByUsername(principal.getName());
		} else {
			user = userService.getOneByUsername(address.getUser().getUsername());
		}
		address.setUser(user);
        return addressService.create(address);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Address address, Principal principal) {
		User user = userService.getOneByUsername(principal.getName());
		JSONObject response = new JSONObject();
		
		Address addressFoundForUser = addressService.getOneByIdForUser(id, user.getId());
		Address addressFound = addressService.getOneById(id);
		
		// If addressFoundForUser is null, means the current user can't access resource if not admin
		if (addressFound != addressFoundForUser && user.getRole() != UserRole.ROLE_ADMIN) {
			response.put("Forbidden", "You cannot access this ressource");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}
				
		if(addressFound == null) {
			response.put("Error", "Address Not Found");
			return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
		}
		
		address = addressService.update(id, address);
		return new ResponseEntity<>(address, HttpStatus.OK);
		
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<JSONObject> delete(@PathVariable("id") Integer id, Principal principal) {
		Boolean success = false;
		User user = userService.getOneByUsername(principal.getName());
		JSONObject response = new JSONObject();
		
		Address addressFoundForUser = addressService.getOneByIdForUser(id, user.getId());
		Address addressFound = addressService.getOneById(id);
		
		// If addressFoundForUser is null, means the current user can't access resource if not admin
		if (addressFound != addressFoundForUser && user.getRole() != UserRole.ROLE_ADMIN) {
			response.put("success", success);
			response.put("Forbidden", "You cannot access this ressource");
			return new ResponseEntity<JSONObject>(response, HttpStatus.FORBIDDEN);
		}
		
		success = addressService.delete(id);
		
		response.put("success", success);
		
		if (success) {
			return new ResponseEntity<JSONObject>(response, HttpStatus.OK);
		}
		response.put("Error", "Address Not Found");
		return new ResponseEntity<JSONObject>(response, HttpStatus.NOT_FOUND);
	}
}
