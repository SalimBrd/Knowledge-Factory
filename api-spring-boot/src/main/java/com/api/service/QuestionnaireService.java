package com.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.api.model.Questionnaire;
import com.api.repositories.QuestionnaireRepository;

@Service
public class QuestionnaireService implements IModelService<Questionnaire> {

	@Autowired
	private QuestionnaireRepository questionnaireRepository;
	
	@Override
	public List<Questionnaire> getList(Integer page, Integer limit) {
		PageRequest pageable = PageRequest.of(page, limit);
		return questionnaireRepository.GetListByPage(pageable);
	}

	@Override
	public Questionnaire getOneById(Integer id) {
		Optional<Questionnaire> questionnaire = questionnaireRepository.findById(id);
		
		if (questionnaire.isEmpty())
			return null;
		
		return questionnaire.get();
	}
	
	public List<Questionnaire> getListForCourse(Integer courseId) {
		return questionnaireRepository.GetListByPageForCourse(courseId);
	}

	@Override
	public Questionnaire create(Questionnaire entity) {
		questionnaireRepository.save(entity);
		return entity;
	}

	@Override
	public Questionnaire update(Integer id, Questionnaire entity) {
		Optional<Questionnaire> questionnaire = questionnaireRepository.findById(id);
		
		if (questionnaire.isEmpty())
			return null;
		
		Questionnaire questionnaireFound = questionnaire.get();
		if(entity.getDescription() != null)
			questionnaireFound.setDescription(entity.getDescription());
		if(entity.getPlacement() != null)
			questionnaireFound.setPlacement(entity.getPlacement());
		if(entity.getTitle() != null)
			questionnaireFound.setTitle(entity.getTitle());
		
		questionnaireRepository.save(questionnaireFound);
		return questionnaireFound;
	}

	@Override
	public boolean delete(Integer id) {
		Optional<Questionnaire> questionnaire = questionnaireRepository.findById(id);
		
		if (questionnaire.isEmpty())
			return false;
		
		try {
			questionnaireRepository.delete(questionnaire.get());
		}
		catch(Exception e) {
			return false;
		}
		
		return true;
	}
}
