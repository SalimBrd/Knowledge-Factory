package com.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.api.model.SurveyAnswer;
import com.api.repositories.SurveyAnswerRepository;

@Service
public class SurveyAnswerService implements IModelService<SurveyAnswer> {

	@Autowired
	private SurveyAnswerRepository surveyAnswerRepository;
	
	@Override
	public List<SurveyAnswer> getList(Integer page, Integer limit) {
		PageRequest pageable = PageRequest.of(page, limit);
		return surveyAnswerRepository.GetListByPage(pageable);
	}
	
	public List<SurveyAnswer> getListForUser(Integer userId) {
		return surveyAnswerRepository.GetListByPageForUser(userId);
	}

	@Override
	public SurveyAnswer getOneById(Integer id) {
		Optional<SurveyAnswer> surveyAnswer = surveyAnswerRepository.findById(id);
		
		if (surveyAnswer.isEmpty())
			return null;
		
		return surveyAnswer.get();
	}
	
	public SurveyAnswer getOneByIdForUser(Integer id, Integer userId) {
		Optional<SurveyAnswer> surveyAnswer = surveyAnswerRepository.GetByIdForUser(id, userId);
		if (surveyAnswer.isEmpty())
			return null;
		
		return surveyAnswer.get();
	}
	
	public SurveyAnswer getOneByUserAndSurveyChoiceId(Integer userId, Integer surveyChoice) {
		Optional<SurveyAnswer> surveyAnswer = surveyAnswerRepository.getOneByUserAndSurveyChoiceId(userId, surveyChoice);
		
		if (surveyAnswer.isEmpty())
			return null;
		
		return surveyAnswer.get();
	}

	@Override
	public SurveyAnswer create(SurveyAnswer entity) {
		surveyAnswerRepository.save(entity);
		return entity;
	}

	@Override
	public SurveyAnswer update(Integer id, SurveyAnswer entity) {
		Optional<SurveyAnswer> surveyAnswer = surveyAnswerRepository.findById(id);
		
		if (surveyAnswer.isEmpty())
			return null;
		
		SurveyAnswer surveyAnswerFound = surveyAnswer.get();
		if(entity.getSurveyChoice() != null)
			surveyAnswerFound.setSurveyChoice(entity.getSurveyChoice());
		
		surveyAnswerRepository.save(surveyAnswerFound);
		return surveyAnswerFound;
	}

	@Override
	public boolean delete(Integer id) {
		Optional<SurveyAnswer> surveyAnswer = surveyAnswerRepository.findById(id);
		
		if (surveyAnswer.isEmpty())
			return false;
		
		try {
			surveyAnswerRepository.delete(surveyAnswer.get());
		}
		catch(Exception e) {
			return false;
		}
		
		return true;
	}
}
