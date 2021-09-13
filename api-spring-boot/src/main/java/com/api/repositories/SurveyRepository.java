package com.api.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.api.model.Survey;

public interface SurveyRepository extends PagingAndSortingRepository<Survey, Integer>{
	
	@Query("SELECT s FROM Survey s ORDER BY s.id DESC")
	List<Survey> GetListByPage(Pageable pageable);
	
	@Query("SELECT s FROM Survey s WHERE s.premium = :premium AND "
			+ "s.activeDate BETWEEN :lastWeek AND :date ORDER BY s.activeDate DESC")
	List<Survey> GetSurveysByType(Boolean premium, Date date, Date lastWeek);
}
