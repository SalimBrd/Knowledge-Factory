package com.api.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.api.model.Survey;
import com.api.repositories.SurveyRepository;

@Service
public class SurveyService implements IModelService<Survey> {

	@Autowired
	private SurveyRepository surveyRepository;
	
	@Override
	public List<Survey> getList(Integer page, Integer limit) {
		PageRequest pageable = PageRequest.of(page, limit);
		return surveyRepository.GetListByPage(pageable);
	}
	
	public List<Survey> getSurveysByType(Boolean premium) {
		Date dateNow = new Date();

		Calendar cal = Calendar.getInstance();
		cal.setTime(dateNow);
		cal.add(Calendar.DAY_OF_MONTH, -7);
		return surveyRepository.GetSurveysByType(premium, dateNow, cal.getTime());
	}

	@Override
	public Survey getOneById(Integer id) {
		Optional<Survey> survey = surveyRepository.findById(id);
		
		if (survey.isEmpty())
			return null;
		
		return survey.get();
	}

	@Override
	public Survey create(Survey entity) {
		surveyRepository.save(entity);
		return entity;
	}

	@Override
	public Survey update(Integer id, Survey entity) {
		Optional<Survey> survey = surveyRepository.findById(id);
		
		if (survey.isEmpty())
			return null;
		
		Survey surveyFound = survey.get();
		if(entity.getTitle() != null)
			surveyFound.setTitle(entity.getTitle());
		if(entity.getDescription() != null)
			surveyFound.setDescription(entity.getDescription());
		if(entity.isPremium() != null)
			surveyFound.setPremium(entity.isPremium());
		if(entity.getActiveDate() != null)
			surveyFound.setActiveDate(entity.getActiveDate());
		
		surveyRepository.save(surveyFound);
		return surveyFound;
	}

	@Override
	public boolean delete(Integer id) {
		Optional<Survey> survey = surveyRepository.findById(id);
		
		if (survey.isEmpty())
			return false;
		
		try {
			surveyRepository.delete(survey.get());
		}
		catch(Exception e) {
			return false;
		}
		
		return true;
	}
}
