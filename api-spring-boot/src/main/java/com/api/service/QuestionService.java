package com.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.api.model.Question;
import com.api.repositories.QuestionRepository;

@Service
public class QuestionService implements IModelService<Question> {

	@Autowired
	private QuestionRepository questionRepository;
	
	@Override
	public List<Question> getList(Integer page, Integer limit) {
		PageRequest pageable = PageRequest.of(page, limit);
		return questionRepository.GetListByPage(pageable);
	}

	@Override
	public Question getOneById(Integer id) {
		Optional<Question> question = questionRepository.findById(id);
		
		if (question.isEmpty())
			return null;
		
		return question.get();
	}

	@Override
	public Question create(Question entity) {
		questionRepository.save(entity);
		return entity;
	}

	@Override
	public Question update(Integer id, Question entity) {
		Optional<Question> question = questionRepository.findById(id);
		
		if (question.isEmpty())
			return null;
		
		Question questionFound = question.get();
		if(entity.getContent() != null)
			questionFound.setContent(entity.getContent());
		if(entity.getPlacement() != null)
			questionFound.setPlacement(entity.getPlacement());
		if(entity.isMultipleAnswers() != null)
			questionFound.setMultipleAnswers(entity.isMultipleAnswers());
		
		questionRepository.save(questionFound);
		return questionFound;
	}

	@Override
	public boolean delete(Integer id) {
		Optional<Question> question = questionRepository.findById(id);
		
		if (question.isEmpty())
			return false;
		
		try {
			questionRepository.delete(question.get());
		}
		catch(Exception e) {
			return false;
		}
		
		return true;
	}
}
