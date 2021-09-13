package com.api.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.api.model.BookOrder;

public interface BookOrderRepository extends PagingAndSortingRepository<BookOrder, Integer>{
	@Query("SELECT b FROM BookOrder b ORDER BY b.id DESC")
	List<BookOrder> GetListByPage(Pageable pageable);
	
	@Query("SELECT b FROM BookOrder b WHERE b.user.id LIKE :userId ORDER BY b.id DESC")
	List<BookOrder> GetListByPageForUser(Pageable pageable, Integer userId);
}
