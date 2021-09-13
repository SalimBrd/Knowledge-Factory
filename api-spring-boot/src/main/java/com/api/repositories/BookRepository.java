package com.api.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.api.model.Book;

public interface BookRepository extends PagingAndSortingRepository<Book, Integer> {
	@Query("SELECT b FROM Book b ORDER BY b.id ASC")
	public List<Book> GetListByPage(Pageable pageable);
}
