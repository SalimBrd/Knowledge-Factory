package com.api.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.api.model.SurveyChoice;

public interface SurveyChoiceRepository extends PagingAndSortingRepository<SurveyChoice, Integer>{
	
	@Query("SELECT s FROM SurveyChoice s ORDER BY s.id DESC")
	List<SurveyChoice> GetListByPage(Pageable pageable);
}
