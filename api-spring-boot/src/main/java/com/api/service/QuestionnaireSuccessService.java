package com.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.api.model.QuestionnaireSuccess;
import com.api.repositories.QuestionnaireSuccessRepository;

@Service
public class QuestionnaireSuccessService implements IModelService<QuestionnaireSuccess> {

	@Autowired
	private QuestionnaireSuccessRepository questionnaireSuccessRepository;
	
	@Override
	public List<QuestionnaireSuccess> getList(Integer page, Integer limit) {
		PageRequest pageable = PageRequest.of(page, limit);
		return questionnaireSuccessRepository.GetListByPage(pageable);
	}

	@Override
	public QuestionnaireSuccess getOneById(Integer id) {
		Optional<QuestionnaireSuccess> questionnaireSuccess = questionnaireSuccessRepository.findById(id);
		
		if (questionnaireSuccess.isEmpty())
			return null;
		
		return questionnaireSuccess.get();
	}
	
	public QuestionnaireSuccess getOneByUserAndQuestionnaireId(Integer userId, Integer questionnaireId) {
		Optional<QuestionnaireSuccess> questionnaireSuccess = 
				questionnaireSuccessRepository.getOneByUserAndQuestionnaireId(userId, questionnaireId);
		
		if (questionnaireSuccess.isEmpty())
			return null;
		
		return questionnaireSuccess.get();
	}
	
	public List<QuestionnaireSuccess> getListForUser(Integer page, Integer limit, Integer userId) {
		PageRequest pageable = PageRequest.of(page, limit);
		return questionnaireSuccessRepository.GetListForUser(pageable, userId);
	}

	public QuestionnaireSuccess getOneByIdForUser(Integer id, Integer userId) {
		Optional<QuestionnaireSuccess> questionnaireSuccess = questionnaireSuccessRepository.GetByIdForUser(id, userId);
		if (questionnaireSuccess.isEmpty())
			return null;
		
		return questionnaireSuccess.get();
	}

	@Override
	public QuestionnaireSuccess create(QuestionnaireSuccess entity) {
		questionnaireSuccessRepository.save(entity);
		return entity;
	}

	@Override
	public boolean delete(Integer id) {
		Optional<QuestionnaireSuccess> questionnaireSuccess = questionnaireSuccessRepository.findById(id);
		
		if (questionnaireSuccess.isEmpty())
			return false;
		
		try {
			questionnaireSuccessRepository.delete(questionnaireSuccess.get());
		}
		catch(Exception e) {
			return false;
		}
		
		return true;
	}

	@Override
	public QuestionnaireSuccess update(Integer id, QuestionnaireSuccess entity) {
		return null;
	}
}
