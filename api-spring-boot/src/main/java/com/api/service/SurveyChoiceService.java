package com.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.api.model.SurveyChoice;
import com.api.repositories.SurveyChoiceRepository;

@Service
public class SurveyChoiceService implements IModelService<SurveyChoice> {

	@Autowired
	private SurveyChoiceRepository surveyChoiceRepository;
	
	@Override
	public List<SurveyChoice> getList(Integer page, Integer limit) {
		PageRequest pageable = PageRequest.of(page, limit);
		return surveyChoiceRepository.GetListByPage(pageable);
	}

	@Override
	public SurveyChoice getOneById(Integer id) {
		Optional<SurveyChoice> surveyChoice = surveyChoiceRepository.findById(id);
		
		if (surveyChoice.isEmpty())
			return null;
		
		return surveyChoice.get();
	}

	@Override
	public SurveyChoice create(SurveyChoice entity) {
		surveyChoiceRepository.save(entity);
		return entity;
	}

	@Override
	public SurveyChoice update(Integer id, SurveyChoice entity) {
		Optional<SurveyChoice> surveyChoice = surveyChoiceRepository.findById(id);
		
		if (surveyChoice.isEmpty())
			return null;
		
		SurveyChoice surveyChoiceFound = surveyChoice.get();
		if(entity.getContent() != null)
			surveyChoiceFound.setContent(entity.getContent());
		if(entity.getPlacement() != null)
			surveyChoiceFound.setPlacement(entity.getPlacement());
		if(entity.getSurvey() != null)
			surveyChoiceFound.setSurvey(entity.getSurvey());
		
		surveyChoiceRepository.save(surveyChoiceFound);
		return surveyChoiceFound;
	}

	@Override
	public boolean delete(Integer id) {
		Optional<SurveyChoice> surveyChoice = surveyChoiceRepository.findById(id);
		
		if (surveyChoice.isEmpty())
			return false;
		
		try {
			surveyChoiceRepository.delete(surveyChoice.get());
		}
		catch(Exception e) {
			return false;
		}
		
		return true;
	}
}
