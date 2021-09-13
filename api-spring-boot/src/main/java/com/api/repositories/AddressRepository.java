package com.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.api.model.Address;

public interface AddressRepository extends PagingAndSortingRepository<Address, Integer> {
		
	@Query("SELECT a FROM Address a ORDER BY a.id ASC")
	public List<Address> GetListByPage(Pageable pageable);
	
	@Query("SELECT a FROM Address a WHERE a.user.id LIKE :userId ORDER BY a.id ASC")
	public List<Address> GetListByPageForUser(Pageable pageable, Integer userId);
	
	@Query("SELECT a FROM Address a WHERE a.user.id LIKE :userId AND a.id LIKE :id ORDER BY a.id ASC")
	public Optional<Address> GetByIdForUser(Integer id, Integer userId);
}
