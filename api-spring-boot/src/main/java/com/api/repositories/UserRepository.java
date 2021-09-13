package com.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.api.model.*;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
	
	@Query("SELECT u FROM User u ORDER BY u.id ASC")
	public List<User> GetListByPage(Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.username LIKE :username")
	public Optional<User> findByUsername(String username);
	
}
