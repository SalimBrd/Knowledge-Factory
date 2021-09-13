package com.api.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.api.model.Answer;

public interface AnswerRepository extends PagingAndSortingRepository<Answer, Integer> {
	@Query("SELECT a FROM Answer a ORDER BY a.id ASC")
	public List<Answer> GetListByPage(Pageable pageable);
}
