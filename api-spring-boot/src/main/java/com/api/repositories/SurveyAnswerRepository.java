package com.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.api.model.SurveyAnswer;

public interface SurveyAnswerRepository extends PagingAndSortingRepository<SurveyAnswer, Integer> {

	@Query("SELECT s FROM SurveyAnswer s ORDER BY s.id DESC")
	List<SurveyAnswer> GetListByPage(Pageable pageable);
	
	@Query("SELECT s FROM SurveyAnswer s WHERE s.user.id LIKE :userId ORDER BY s.id ASC")
	List<SurveyAnswer> GetListByPageForUser(Integer userId);

	@Query("SELECT s FROM SurveyAnswer s Where s.user.id LIKE :userId AND s.surveyChoice.id LIKE :surveyChoiceId")
	Optional<SurveyAnswer> getOneByUserAndSurveyChoiceId(Integer userId, Integer surveyChoiceId);
	
	@Query("SELECT s FROM SurveyAnswer s WHERE s.user.id LIKE :userId AND s.id LIKE :id")
	Optional<SurveyAnswer> GetByIdForUser(Integer id, Integer userId);
	
}
