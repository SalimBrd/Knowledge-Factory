package com.api.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.api.model.Questionnaire;

public interface QuestionnaireRepository extends PagingAndSortingRepository<Questionnaire, Integer> {
	@Query("SELECT q FROM Questionnaire q WHERE q.course.id LIKE :courseId ORDER BY q.placement ASC")
	public List<Questionnaire> GetListByPageForCourse(Integer courseId);

	@Query("SELECT q from Questionnaire q ORDER BY q.id DESC")
	public List<Questionnaire> GetListByPage(Pageable pageable);
}
