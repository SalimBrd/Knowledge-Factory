package com.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.api.model.Diploma;
import com.api.repositories.DiplomaRepository;

@Service
public class DiplomaService implements IModelService<Diploma> {

	@Autowired
	private DiplomaRepository diplomaRepository;
	
	public List<Diploma> getListForUser(Integer page, Integer limit, Integer userId) {
		PageRequest pageable = PageRequest.of(page, limit);
		return diplomaRepository.GetListByPageForUser(pageable, userId);
	}

	@Override
	public Diploma getOneById(Integer id) {
		Optional<Diploma> diploma = diplomaRepository.findById(id);
		
		if (diploma.isEmpty())
			return null;
		
		return diploma.get();
	}
	
	public Diploma getOneByIdForUser(Integer id, Integer userId) {
		Optional<Diploma> diploma = diplomaRepository.GetByIdForUser(id, userId);
		if (diploma.isEmpty())
			return null;
		
		return diploma.get();
	}
	
	public Diploma getOneByUserAndCourseId(Integer userId, Integer courseId) {
		Optional<Diploma> diploma = diplomaRepository.getOneByUserAndCourseId(userId, courseId);
		
		if (diploma.isEmpty())
			return null;
		
		return diploma.get();
	}

	@Override
	public Diploma create(Diploma entity) {
		diplomaRepository.save(entity);
		return entity;
	}

	@Override
	public boolean delete(Integer id) {
		Optional<Diploma> diploma = diplomaRepository.findById(id);
		
		if (diploma.isEmpty())
			return false;
		
		try {
			diplomaRepository.delete(diploma.get());
		}
		catch(Exception e) {
			return false;
		}
		
		return true;
	}

	@Override
	public List<Diploma> getList(Integer page, Integer limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Diploma update(Integer id, Diploma entity) {
		// TODO Auto-generated method stub
		return null;
	}
}
