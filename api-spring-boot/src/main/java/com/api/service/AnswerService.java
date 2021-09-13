package com.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.api.model.Answer;
import com.api.repositories.AnswerRepository;

@Service
public class AnswerService implements IModelService<Answer> {

	@Autowired
	private AnswerRepository answerRepository;
	
	@Override
	public List<Answer> getList(Integer page, Integer limit) {
		PageRequest pageable = PageRequest.of(page, limit);
		return answerRepository.GetListByPage(pageable);
	}

	@Override
	public Answer getOneById(Integer id) {
		Optional<Answer> answer = answerRepository.findById(id);
		
		if (answer.isEmpty())
			return null;
		
		return answer.get();
	}

	@Override
	public Answer create(Answer entity) {
		System.out.println(entity);
		answerRepository.save(entity);
		return entity;
	}

	@Override
	public Answer update(Integer id, Answer entity) {
		Optional<Answer> answer = answerRepository.findById(id);
		
		if (answer.isEmpty())
			return null;
		
		Answer answerFound = answer.get();
		if(entity.getContent() != null)
			answerFound.setContent(entity.getContent());
		if(entity.getPlacement() != null)
			answerFound.setPlacement(entity.getPlacement());
		if(entity.isSuccess() != null)
			answerFound.setSuccess(entity.isSuccess());
			
		
		answerRepository.save(answerFound);
		return answerFound;
	}

	@Override
	public boolean delete(Integer id) {
		Optional<Answer> answer = answerRepository.findById(id);
		
		if (answer.isEmpty())
			return false;
		
		try {
			answerRepository.delete(answer.get());
		}
		catch(Exception e) {
			return false;
		}
		
		return true;
	}
}
