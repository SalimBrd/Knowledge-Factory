package com.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.api.model.QuestionnaireSuccess;

public interface QuestionnaireSuccessRepository extends PagingAndSortingRepository<QuestionnaireSuccess, Integer> {
	@Query("SELECT q FROM QuestionnaireSuccess q ORDER BY q.id DESC")
	List<QuestionnaireSuccess> GetListByPage(Pageable pageable);
	
	@Query("SELECT q FROM QuestionnaireSuccess q WHERE q.user.id LIKE :userId")
	List<QuestionnaireSuccess>GetListForUser(Pageable pageable, Integer userId);
	
	@Query("SELECT q FROM QuestionnaireSuccess q WHERE q.user.id LIKE :userId AND q.id LIKE :id ORDER BY q.id ASC")
	Optional<QuestionnaireSuccess> GetByIdForUser(Integer id, Integer userId);
	
	@Query("SELECT q FROM QuestionnaireSuccess q Where q.user.id LIKE :userId AND q.questionnaire.id LIKE :questionnaireId")
	Optional<QuestionnaireSuccess> getOneByUserAndQuestionnaireId(Integer userId, Integer questionnaireId);
}
