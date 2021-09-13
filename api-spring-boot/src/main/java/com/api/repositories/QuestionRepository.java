package com.api.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.api.model.Question;

public interface QuestionRepository extends PagingAndSortingRepository<Question, Integer> {
	@Query("SELECT q FROM Question q ORDER BY q.id ASC")
	public List<Question> GetListByPage(Pageable pageable);
}
