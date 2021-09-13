package com.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.api.model.Course;
import com.api.repositories.CourseRepository;

@Service
public class CourseService implements IModelService<Course> {

	@Autowired
	private CourseRepository courseRepository;
	
	@Override
	public List<Course> getList(Integer page, Integer limit) {
		PageRequest pageable = PageRequest.of(page, limit);
		return courseRepository.GetListByPage(pageable);
	}
	
	public List<Course> getCourseByType(Boolean premium, Integer page, Integer limit) {
		PageRequest pageable = PageRequest.of(page, limit);
		return courseRepository.GetCourseListByPage(premium, pageable);
	}

	@Override
	public Course getOneById(Integer id) {
		Optional<Course> course = courseRepository.findById(id);
		
		if (course.isEmpty())
			return null;
		
		return course.get();
	}

	@Override
	public Course create(Course entity) {
		courseRepository.save(entity);
		return entity;
	}

	@Override
	public Course update(Integer id, Course entity) {
		Optional<Course> course = courseRepository.findById(id);
		
		if (course.isEmpty())
			return null;
		
		Course courseFound = course.get();
		if(entity.getTitle() != null)
			courseFound.setTitle(entity.getTitle());
		if(entity.getDifficulty() != null)
			courseFound.setDifficulty(entity.getDifficulty());
		if(entity.getSuggestedHours() != null)
			courseFound.setSuggestedHours(entity.getSuggestedHours());
		if(entity.isPremium() != null)
			courseFound.setPremium(entity.isPremium());
		
		courseRepository.save(courseFound);
		return courseFound;
	}

	@Override
	public boolean delete(Integer id) {
		Optional<Course> course = courseRepository.findById(id);
		
		if (course.isEmpty())
			return false;
		
		try {
			courseRepository.delete(course.get());
		}
		catch(Exception e) {
			return false;
		}
		
		return true;
	}
}
