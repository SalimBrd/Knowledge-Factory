package com.api.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.api.model.Course;

public interface CourseRepository extends PagingAndSortingRepository<Course, Integer> {
	@Query("SELECT c FROM Course c ORDER BY c.creationDate DESC")
	public List<Course> GetListByPage(Pageable pageable);
	
	@Query("SELECT c FROM Course c WHERE c.premium = :premium ORDER BY c.creationDate DESC")
	public List<Course> GetCourseListByPage(Boolean premium, Pageable pageable);
}
