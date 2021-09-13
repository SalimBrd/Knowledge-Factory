package com.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.model.User;
import com.api.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<User> getList(Integer page, Integer limit) {
		PageRequest pageable = PageRequest.of(page, limit);
		return userRepository.GetListByPage(pageable);
	}

	public User getOneByUsername(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		
		if (user.isEmpty())
			return null;
		
		return user.get();
	}
	
	public User getOneById(Integer id) {
		Optional<User> user = userRepository.findById(id);
		
		if (user.isEmpty())
			return null;
		
		return user.get();
	}

	public User update(Integer id, User entity) {
		Optional<User> user = userRepository.findById(id);
		
		if (user.isEmpty())
			return null;
		
		
		User userFound = user.get();
		if(entity.getUsername() != null && entity.getUsername() != "")
			userFound.setUsername(entity.getUsername());
		if(entity.getPassword() != null && entity.getPassword() != "") {
			String passwordHashed = passwordEncoder.encode(entity.getPassword());
			userFound.setPassword(passwordHashed);
		}
		if(entity.getRole() != null)
			userFound.setRole(entity.getRole());

		userRepository.save(userFound);
		return userFound;
	}

	public boolean delete(Integer id) {
		Optional<User> user = userRepository.findById(id);
		
		if (user.isEmpty())
			return false;
		
		try {
			userRepository.delete(user.get());
		}
		catch(Exception e) {
			return false;
		}
		
		return true;
	}

}
